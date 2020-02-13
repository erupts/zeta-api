package xyz.erupt.openApi.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import xyz.erupt.openApi.impl.OpenApi;
import xyz.erupt.openApi.handler.OpenApiHandler;
import xyz.erupt.openApi.tag.IfTag;
import xyz.erupt.openApi.tag.RootTag;
import xyz.erupt.openApi.tag.EleTag;
import xyz.erupt.openApi.util.OpenApiSpringUtil;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

/**
 * @author liyuepeng
 * @date 2019-12-22
 */
@Service
public class OpenApiService {

    @Autowired
    private HttpServletRequest request;

    @Value("${openApi.hotReadXml:false}")
    private boolean hotReadXml;

    @Value("${openApi.xmlbasePath:epi}")
    private String xmlBasePath;

    private Map<String, Cache<String, Object>> cacheMap = new HashMap<>();

    private Map<String, Document> xmlDocuments = new HashMap<>();

    private Document getXmlDocument(String fileName) {
        try {
            if (hotReadXml) {
                Resource resource = new ClassPathResource(xmlBasePath + "/" + fileName + ".xml");
                return new SAXReader().read(resource.getFile());
            } else {
                if (xmlDocuments.containsKey(fileName)) {
                    return xmlDocuments.get(fileName);
                } else {
                    Resource resource = new ClassPathResource(xmlBasePath + "/" + fileName + ".xml");
                    Document document = new SAXReader().read(resource.getFile());
                    xmlDocuments.put(fileName, document);
                    return document;
                }
            }
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Object modify(String fileName, String elementName, OpenApi openApi) {
        return xmlToQuery(fileName, elementName,
                (element, expression) -> openApi.modify(element, expression, getReqMap()));
    }

    public Object queryByCache(String fileName, String elementName, OpenApi openApi) {
        return xmlToQuery(fileName, elementName, (element, expression) -> {
            Attribute cacheAttr = element.attribute(EleTag.CACHE);
            if (null != cacheAttr) {
                String cacheKey = fileName + "_" + elementName;
                Cache<String, Object> cache = cacheMap.get(cacheKey);
                if (null == cache) {
                    cache = Caffeine.newBuilder()
                            .initialCapacity(1).maximumSize(100)
                            .expireAfterWrite(Long.valueOf(cacheAttr.getValue()), TimeUnit.MILLISECONDS)
                            .build();
                    cacheMap.put(fileName + "_" + elementName, cache);
                }
                Map<String, String> param = getReqMap();
                StringBuilder paramKey = new StringBuilder();
                for (String key : param.keySet()) {
                    paramKey.append(key).append("=").append(param.get(key)).append("|");
                }
                return cache.get(paramKey.toString(), (key) -> openApi.query(element, expression, param));
            } else {
                return openApi.query(element, expression, getReqMap());
            }
        });
    }


    public Object xmlToQuery(String fileName, String elementName, BiFunction<Element, String, Object> function) {
        Element rootElement = getXmlDocument(fileName).getRootElement();
        Element element = rootElement.element(elementName);
        String expression = parseElement(element);
        OpenApiHandler openApiHandler = getRootHandler(rootElement);
        if (null != openApiHandler) {
            expression = openApiHandler.handler(element, expression);
        }
        Object result = function.apply(element, expression);
        if (null != openApiHandler) {
            return openApiHandler.handlerResult(element, result);
        }
        return result;
    }

    private String parseElement(Element element) {
        String eval = element.getTextTrim();
        List<Element> list = element.elements();
        if (list.size() > 0) {
            StringBuilder sb = new StringBuilder(eval);
            ScriptEngine js = new ScriptEngineManager().getEngineByName("JavaScript");
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String parameterName = parameterNames.nextElement();
                js.put(parameterName,request.getParameter(parameterName));
            }
            for (Element ele : list) {
                if (IfTag.NAME.equals(ele.getName())) {
                    String value = ele.attribute(IfTag.ATTR_TEST).getValue();
                    try {
                        Boolean bool = (Boolean) js.eval("!!(" + value + ")");
                        if (bool) {
                            sb.append(" ").append(ele.getTextTrim());
                        }
                    } catch (ScriptException e) {
                        e.printStackTrace();
                    }
                }
            }
            return sb.toString();
        } else {
            return eval;
        }
    }


    private OpenApiHandler getRootHandler(Element element) {
        Attribute handlerAttr = element.attribute(RootTag.HANDLER);
        if (null != handlerAttr) {
            try {
                return (OpenApiHandler) OpenApiSpringUtil.getBean(Class.forName(handlerAttr.getValue()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

//    private static final String COLON = ":";

    public Map<String, String> getReqMap() {
        Enumeration<String> parameterNames = request.getParameterNames();
        Map<String, String> map = new TreeMap<>();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            String val = request.getParameter(parameterName);
            if (StringUtils.isBlank(val)) {
                val = "";
            }
            map.put(parameterName, val);
        }
        return map;
    }
}
