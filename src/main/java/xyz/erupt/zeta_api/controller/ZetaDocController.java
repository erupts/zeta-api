package xyz.erupt.zeta_api.controller;

import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.erupt.zeta_api.config.ZetaApiConfig;
import xyz.erupt.zeta_api.constant.PathConst;
import xyz.erupt.zeta_api.constant.TagConst;
import xyz.erupt.zeta_api.service.ZetaApiService;
import xyz.erupt.zeta_api.tag.EleTag;
import xyz.erupt.zeta_api.util.IpUtil;
import xyz.erupt.zeta_api.vo.ParamVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liyuepeng
 * @date 2020-06-16
 */
@Log
@RestController
@RequestMapping(PathConst.ZETA_DOC)
public class ZetaDocController {

    @Autowired
    private ZetaApiConfig zetaApiConfig;

    @Autowired
    private ZetaApiService zetaApiService;

    private static final String DOCUMENT_PATH = "/zeta-doc.html";

    private static String page;

    static {
        InputStream stream = ZetaDocController.class.getResourceAsStream(DOCUMENT_PATH);
        try {
            page = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @GetMapping(value = "/{fileName}.html", produces = "text/html;charset=utf-8")
    public String doc(HttpServletResponse response, HttpServletRequest request,
                      @PathVariable("fileName") String fileName) {
        InputStream stream = ZetaDocController.class.getResourceAsStream(DOCUMENT_PATH);
        try {
            return StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stream.close();
        }
        return page;
    }

    @GetMapping(value = "/build/doc/{fileName}")
    public Map<String, Object> apiDoc(HttpServletResponse response, HttpServletRequest request,
                                      @PathVariable("fileName") String fileName) {
        if (!zetaApiConfig.isEnableApiDoc()) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return null;
        }
        if (!zetaApiService.validateIpWhite()) {
            log.warning(IpUtil.getIpAddr(request) + " ip block");
            return null;
        }
        Document document = zetaApiService.getXmlDocument(fileName);
        if (null != document) {
            Map<String, Object> map = new HashMap<>();
            List<Map<String, Object>> eleList = new ArrayList<>();
            Map<String, Object> eleMap;
            Element rootElement = document.getRootElement();
            for (Element element : document.getRootElement().elements()) {
                eleMap = new HashMap<>();
                eleMap.put("name", element.getName());
                eleMap.put("path", PathConst.ZETA_API + "/sql/" + fileName + "/" + element.getName());
                eleMap.put(EleTag.CACHE, element.attributeValue(EleTag.CACHE));
                eleMap.put(EleTag.TITLE, element.attributeValue(EleTag.TITLE));
                eleMap.put(EleTag.DESC, element.attributeValue(EleTag.DESC));
                List<Element> paramsEle = element.elements(TagConst.ParamTag.NAME);
                if (paramsEle.size() > 0) {
                    List<ParamVO> paramVOList = new ArrayList<>();
                    for (Element param : paramsEle) {
                        ParamVO paramVO = new ParamVO();
                        paramVO.setValue(param.attributeValue(TagConst.ParamTag.ATTR_DEFAULT_VALUE));
                        paramVO.setKey(param.attributeValue(TagConst.ParamTag.ATTR_KEY));
                        paramVO.setTitle(param.attributeValue(TagConst.ParamTag.ATTR_TITLE));
                        paramVOList.add(paramVO);
                    }
                    eleMap.put("params", new GsonBuilder().serializeNulls().create().toJson(paramVOList));
                }
                eleList.add(eleMap);
            }
            String desc = rootElement.attributeValue("desc");
            map.put("desc", StringUtils.isNotBlank(desc) ? desc : fileName);
            map.put("elements", eleList);
            map.put("fileName", fileName);
            map.put("domain", zetaApiConfig.getDomain() != null ? zetaApiConfig.getDomain() : request.getRequestURL().toString().split(PathConst.ZETA_DOC)[0]);
            return map;
        } else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }
    }
}
