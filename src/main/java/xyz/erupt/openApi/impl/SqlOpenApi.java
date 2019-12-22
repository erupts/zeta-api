package xyz.erupt.openApi.impl;

import lombok.extern.java.Log;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
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
    public Object query(Element element, String sql, Map<String, String> params) {
        log.info(sql);
        return namedTemplate.queryForList(sql, params);
    }


}