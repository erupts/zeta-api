package xyz.erupt.openApi.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import xyz.erupt.openApi.config.OpenApiConfig;
import xyz.erupt.openApi.service.OpenApiService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liyuepeng
 * @date 2020-06-16
 */
@Controller
public class ApiDoc {

    @Autowired
    private OpenApiConfig openApiConfig;

    @Autowired
    private OpenApiService openApiService;


    @GetMapping(value = "/api-doc/{fileName}", produces = "text/html;charset=utf-8")
    public void apiDoc(HttpServletResponse response, HttpServletRequest request, @PathVariable("fileName") String fileName) {
        if (!openApiConfig.isApiDoc()) {
            response.setStatus(401);
            return;
        }
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);
            cfg.setClassForTemplateLoading(this.getClass(), "/");
            cfg.setDefaultEncoding("utf-8");
            Template emailTemplate = cfg.getTemplate("/api-doc.ftl");
            Document document = openApiService.getXmlDocument(fileName);
            if (null != document) {
                Map<String, Object> map = new HashMap<>();
                map.put("root", document.getRootElement());
                map.put("domain", request.getRequestURL().substring(0,
                        request.getRequestURL().indexOf(request.getServletPath())) +
                        "/open-api/sql/query/" + fileName + "/");
                response.setCharacterEncoding("utf-8");
                emailTemplate.process(map, response.getWriter());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
