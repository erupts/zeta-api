package xyz.erupt.openApi.impl;

import lombok.extern.java.Log;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import xyz.erupt.openApi.util.IpUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @author liyuepeng
 * @date 2019-12-22
 */
@Component
@Log
public class SqlOpenApi implements OpenApi {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private NamedParameterJdbcTemplate namedTemplate;


    @Override
    public Object query(Element element, String sql, Map<String, Object> params) {
        log.info(sql);
//        log.info("[" + new Date() + ":" + IpUtil.getIpAddr(request) + "]" + params);
        return namedTemplate.queryForList(sql, params);
    }

    @Override
    public Object modify(Element element, String sql, Map<String, Object> params) {
        log.info(sql);
        return namedTemplate.update(sql, params);
//        try {
//            return namedTemplate.update(sql, params);
//        } catch (InvalidDataAccessApiUsageException e) {
//            throw new RuntimeException("参数语法错误");
//        } catch (Exception e) {
//            throw new RuntimeException("后端语法错误");
//        }
    }


}