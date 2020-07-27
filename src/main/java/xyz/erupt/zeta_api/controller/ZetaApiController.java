package xyz.erupt.zeta_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.erupt.zeta_api.constant.PathConst;
import xyz.erupt.zeta_api.impl.SqlZetaApi;
import xyz.erupt.zeta_api.service.ZetaApiService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liyuepeng on 2019-08-14.
 */
@RestController
@RequestMapping(PathConst.ZETA_API)
public class ZetaApiController {

    @Autowired
    private SqlZetaApi zetaApi;

    @Autowired
    private ZetaApiService zetaApiService;

    /**
     * @param fileName   文件名称
     * @param elementName xml中sql元素
     * @return result
     */
    @RequestMapping("/sql/{fileName}/{elementName}")
    @ResponseBody
    public Map<String, Object> action(@PathVariable("fileName") String fileName,
                                      @PathVariable("elementName") String elementName,
                                      @RequestBody(required = false) Map<String, Object> params,
                                      HttpServletRequest request) {
        if (!zetaApiService.validateIpWhite()) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        request.setAttribute(ZetaApiService.REQUEST_BODY_KEY, params);
        map.put("result", zetaApiService.action(fileName, elementName, zetaApi, params));
        return map;
    }


}
