package xyz.erupt.openApi.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.erupt.openApi.config.OpenApiConfig;
import xyz.erupt.openApi.constant.PathConst;
import xyz.erupt.openApi.service.OpenApiService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liyuepeng
 * @date 2020-06-16
 */
@RestController
@RequestMapping(PathConst.OPEN_API)
public class ApiDocument {

    @Autowired
    private OpenApiConfig openApiConfig;

    @Autowired
    private OpenApiService openApiService;

    public static final String DOCUMENT_FTL_PATH = "/api-doc.ftl";

    private static Configuration cfg;

    static {
        cfg = new Configuration(Configuration.VERSION_2_3_27);
        cfg.setClassForTemplateLoading(ApiDocument.class, "/");
        cfg.setDefaultEncoding("utf-8");
    }

    @GetMapping(value = "/doc/{fileName}.html", produces = "text/html;charset=utf-8")
    public void apiDoc(HttpServletResponse response, HttpServletRequest request, @PathVariable("fileName") String fileName) {
        if (!openApiConfig.isEnableApiDoc()) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }
        if (!openApiService.validateIpWhite()) {
            return;
        }
        try {
            Template template = cfg.getTemplate(DOCUMENT_FTL_PATH);
            Document document = openApiService.getXmlDocument(fileName);
            if (null != document) {
                Map<String, Object> map = new HashMap<>();
                map.put("root", document.getRootElement());
                map.put("domain", request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getServletPath())));
                map.put("fileName", fileName);
                response.setCharacterEncoding("utf-8");
                template.process(map, response.getWriter());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
