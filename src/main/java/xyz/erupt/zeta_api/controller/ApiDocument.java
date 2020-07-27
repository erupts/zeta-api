package xyz.erupt.zeta_api.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.erupt.zeta_api.config.ZetaApiConfig;
import xyz.erupt.zeta_api.constant.PathConst;
import xyz.erupt.zeta_api.service.ZetaApiService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liyuepeng
 * @date 2020-06-16
 */
@RestController
@RequestMapping(PathConst.ZETA_API)
public class ApiDocument {

    @Autowired
    private ZetaApiConfig zetaApiConfig;

    @Autowired
    private ZetaApiService zetaApiService;

    private static final String DOCUMENT_FTL_PATH = "/zeta-doc.ftl";

    private static Configuration cfg;

    static {
        cfg = new Configuration(Configuration.VERSION_2_3_27);
        cfg.setClassForTemplateLoading(ApiDocument.class, "/");
        cfg.setDefaultEncoding("utf-8");
    }

    @GetMapping(value = "/doc/{fileName}.html", produces = "text/html;charset=utf-8")
    public void apiDoc(HttpServletResponse response, HttpServletRequest request, @PathVariable("fileName") String fileName) {
        if (!zetaApiConfig.isEnableApiDoc()) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }
        if (!zetaApiService.validateIpWhite()) {
            return;
        }
        try {
            Template template = cfg.getTemplate(DOCUMENT_FTL_PATH);
            Document document = zetaApiService.getXmlDocument(fileName);
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
