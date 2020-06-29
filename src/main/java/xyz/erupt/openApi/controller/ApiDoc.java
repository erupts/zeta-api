package xyz.erupt.openApi.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import xyz.erupt.openApi.config.ApiDocConfig;
import xyz.erupt.openApi.service.OpenApiService;
import xyz.erupt.openApi.util.IpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liyuepeng
 * @date 2020-06-16
 */
@Controller
public class ApiDoc {

    @Autowired
    private ApiDocConfig apiDocConfig;

    @Autowired
    private OpenApiService openApiService;

    private static Configuration cfg;

    static {
        cfg = new Configuration(Configuration.VERSION_2_3_27);
        cfg.setClassForTemplateLoading(ApiDoc.class, "/");
        cfg.setDefaultEncoding("utf-8");
    }

    @GetMapping(value = "/api-doc/{fileName}.html", produces = "text/html;charset=utf-8")
    public void apiDoc(HttpServletResponse response, HttpServletRequest request, @PathVariable("fileName") String fileName) {
        if (!apiDocConfig.isEnable()) {
            response.setStatus(401);
            return;
        }
        if (null != apiDocConfig.getIpWhite() && apiDocConfig.getIpWhite().size() > 0) {
            String reqIp = IpUtil.getIpAddr(request);
            boolean ipAllow = false;
            for (String ip : apiDocConfig.getIpWhite()) {
                if (ip.equals(reqIp)) {
                    ipAllow = true;
                }
            }
            if (!ipAllow) {
                return;
            }
        }
        try {
            Template template = cfg.getTemplate("/api-doc.ftl");
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
