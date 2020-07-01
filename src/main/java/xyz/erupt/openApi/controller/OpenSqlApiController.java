package xyz.erupt.openApi.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import xyz.erupt.openApi.impl.SqlOpenApi;
import xyz.erupt.openApi.service.OpenApiService;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * Created by liyuepeng on 2019-08-14.
 */
@RestController
@RequestMapping("/open-api/sql")
public class OpenSqlApiController {

    @Autowired
    private SqlOpenApi sqlOpenApi;

    @Autowired
    private OpenApiService openApiService;

    /**
     * @param fileName   文件名称
     * @param sqlElement xml中sql元素
     * @return result
     */
    @RequestMapping("/query/{fileName}/{elementName}")
    @ResponseBody
    public Object query(@PathVariable("fileName") String fileName,
                         @PathVariable("elementName") String elementName,
                         @RequestBody(required = false) Map<String, Object> params,
                         HttpServletRequest request) {
        request.setAttribute(OpenApiService.REQUEST_BODY_KEY, params);
        return openApiService.queryByCache(fileName, elementName, sqlOpenApi, params);
    }

    @RequestMapping("/modify/{fileName}/{elementName}")
    @ResponseBody
    @Transactional
    public Object modify(@PathVariable("fileName") String fileName,
                         @PathVariable("elementName") String elementName,
                         @RequestBody(required = false) Map<String, Object> params,
                         HttpServletRequest request) {
        request.setAttribute(OpenApiService.REQUEST_BODY_KEY, params);
        return openApiService.modify(fileName, elementName, sqlOpenApi, params);
    }

}
