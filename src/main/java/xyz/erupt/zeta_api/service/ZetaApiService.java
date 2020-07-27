package xyz.erupt.zeta_api.service;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import xyz.erupt.zeta_api.config.ZetaApiConfig;
import xyz.erupt.zeta_api.handler.CacheHandler;
import xyz.erupt.zeta_api.handler.ZetaApiHandler;
import xyz.erupt.zeta_api.impl.ZetaApi;
import xyz.erupt.zeta_api.tag.EleTag;
import xyz.erupt.zeta_api.tag.IfTag;
import xyz.erupt.zeta_api.tag.RootTag;
import xyz.erupt.zeta_api.util.IpUtil;
import xyz.erupt.zeta_api.util.NotFountException;
import xyz.erupt.zeta_api.util.ZetaApiSpringUtil;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

/**
 * @author liyuepeng
 * @date 2019-12-22
 */
@Service
public class ZetaApiService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private ZetaApiConfig zetaApiConfig;

    private final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

    public static final String REQUEST_BODY_KEY = "$requestBody";

    private static final String QUERY_FEATURES = "select";

    private Map<String, Document> xmlDocuments = new ConcurrentHashMap<>();

    public Object action(String fileName, String elementName, ZetaApi zetaApi, Map<String, Object> params) {
        return xmlToQuery(fileName, elementName, (element, expression) -> {
//            Attribute typeAttr = element.attribute(EleTag.TYPE);
            if (expression.startsWith(QUERY_FEATURES)) {
                Attribute cacheAttr = element.attribute(EleTag.CACHE);
                if (null != cacheAttr && zetaApiConfig.isEnableCache()) {
                    {
                        String cacheKey = fileName + "_" + elementName;
                        CacheHandler cacheHandler = null;
                        try {
                            cacheHandler = ZetaApiSpringUtil.getBeanByPath(zetaApiConfig.getCacheHandlerPath(), CacheHandler.class);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        Object result = cacheHandler.get(cacheKey, params);
                        if (null == result) {
                            result = zetaApi.query(element, expression, params);
                            cacheHandler.put(cacheKey, params, result, Long.valueOf(cacheAttr.getValue()));
                        }
                        return result;
                    }
                } else {
                    return zetaApi.query(element, expression, params);
                }
            } else {
                return zetaApi.modify(element, expression, params);
            }
        });
    }

    //    校验ip白名单
    public boolean validateIpWhite() {
        List<String> ipWhite = zetaApiConfig.getIpWhite();
        if (null != ipWhite && ipWhite.size() > 0) {
            String reqIp = IpUtil.getIpAddr(request);
            for (String ip : ipWhite) {
                if (ip.equals(reqIp)) {
                    return true;
                }
            }
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        } else {
            return true;
        }
    }

    public Document getXmlDocument(String fileName) {
        try {
            if (zetaApiConfig.isHotReadXml()) {
                return new SAXReader().read(this.getClass().getResourceAsStream(
                        zetaApiConfig.getXmlbasePath() + "/" + fileName + ".xml"));
            } else {
                if (xmlDocuments.containsKey(fileName)) {
                    return xmlDocuments.get(fileName);
                } else {
                    Document document = new SAXReader().read(this.getClass().getResourceAsStream(
                            zetaApiConfig.getXmlbasePath() + "/" + fileName + ".xml"));
                    xmlDocuments.put(fileName, document);
                    return document;
                }
            }
        } catch (DocumentException e) {
            if (e.getCause() instanceof MalformedURLException) {
                throw new NotFountException("not found " + fileName + " document");
            } else {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    private Object xmlToQuery(String fileName, String elementName, BiFunction<Element, String, Object> function) {
        Element rootElement = getXmlDocument(fileName).getRootElement();
        Element element = rootElement.element(elementName);
        if (null == element) {
            throw new NotFountException("not found " + elementName + " element");
        }
        String expression = parseElement(element);
        ZetaApiHandler zetaApiHandler = getRootHandler(rootElement);
        if (null != zetaApiHandler) {
            expression = zetaApiHandler.handler(element, expression);
        }
        Object result = function.apply(element, expression);
        if (null != zetaApiHandler) {
            return zetaApiHandler.handlerResult(element, result);
        }
        return result;
    }

    @SneakyThrows
    private String parseElement(Element element) {
        String content = element.getTextTrim();
        List<Element> list = element.elements();
        if (list.size() > 0) {
            StringBuilder sb = new StringBuilder(content);
            ScriptEngine js = scriptEngineManager.getEngineByName("JavaScript");
            Object paramObj = request.getAttribute(ZetaApiService.REQUEST_BODY_KEY);
            if (paramObj != null) {
                Map<String, Object> param = (Map<String, Object>) paramObj;
                for (String key : param.keySet()) {
                    js.put(key, param.get(key));
                }
            }
            for (Element ele : list) {
                if (IfTag.NAME.equals(ele.getName())) {
                    Attribute testAttr = ele.attribute(IfTag.ATTR_TEST);
                    if (null != testAttr) {
                        if ((Boolean) js.eval("!!(" + testAttr.getValue() + ")")) {
                            sb.append(" ").append(ele.getTextTrim());
                        }
                    } else {
                        sb.append(" ").append(ele.getTextTrim());
                    }
                }
            }
            return sb.toString();
        } else {
            return content;
        }
    }


    private ZetaApiHandler getRootHandler(Element element) {
        Attribute handlerAttr = element.attribute(RootTag.HANDLER);
        if (null != handlerAttr) {
            try {
                return (ZetaApiHandler) ZetaApiSpringUtil.getBean(Class.forName(handlerAttr.getValue()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

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
