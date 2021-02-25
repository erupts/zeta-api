package xyz.erupt.zeta_api.controller;

import org.springframework.web.bind.annotation.*;
import xyz.erupt.zeta_api.constant.PathConst;
import xyz.erupt.zeta_api.impl.SqlZetaApi;
import xyz.erupt.zeta_api.service.ZetaApiService;
import xyz.erupt.zeta_api.vo.ResultVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liyuepeng on 2019-08-14.
 */
@RestController
@RequestMapping(PathConst.ZETA_API)
public class ZetaApiController {

    @Resource
    private SqlZetaApi zetaApi;

    @Resource
    private ZetaApiService zetaApiService;

    /**
     * @param fileName    文件名称
     * @param elementName xml中sql元素
     * @return result
     */
    @RequestMapping("/sql/{fileName}/{elementName}")
    @ResponseBody
    public ResultVo action(@PathVariable("fileName") String fileName,
                           @PathVariable("elementName") String elementName,
                           @RequestBody(required = false) Map<String, Object> params) {
        if (!zetaApiService.validateIpWhite()) {
            return null;
        }
        ResultVo resultVo = new ResultVo();
        resultVo.setResult(zetaApiService.action(fileName, elementName, zetaApi, params, resultVo.getMap()));
        resultVo.setSuccess(true);
        return resultVo;
    }


}
