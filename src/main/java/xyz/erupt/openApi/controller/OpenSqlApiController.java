package xyz.erupt.openApi.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import xyz.erupt.openApi.constant.PathConst;
import xyz.erupt.openApi.impl.SqlOpenApi;
import xyz.erupt.openApi.service.OpenApiService;
import xyz.erupt.openApi.util.IpUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liyuepeng on 2019-08-14.
 */
@RestController
@RequestMapping(PathConst.OPEN_API)
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
    @RequestMapping("/sql/{fileName}/{elementName}")
    @ResponseBody
    public Map<String, Object> action(@PathVariable("fileName") String fileName,
                                      @PathVariable("elementName") String elementName,
                                      @RequestBody(required = false) Map<String, Object> params,
                                      HttpServletRequest request) {
        if (!openApiService.validateIpWhite()) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        request.setAttribute(OpenApiService.REQUEST_BODY_KEY, params);
        map.put("result", openApiService.action(fileName, elementName, sqlOpenApi, params));
        return map;
    }


}
