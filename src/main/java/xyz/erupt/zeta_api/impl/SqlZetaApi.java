package xyz.erupt.zeta_api.impl;

import lombok.extern.java.Log;
import org.dom4j.Element;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import xyz.erupt.zeta_api.config.ZetaApiProp;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author liyuepeng
 * @date 2019-12-22
 */
@Component
@Log
public class SqlZetaApi implements ZetaApi {


    @Resource
    private NamedParameterJdbcTemplate namedTemplate;

    @Resource
    private ZetaApiProp zetaApiProp;

    @Override
    public List<Map<String, Object>> query(Element element, String sql, Map<String, Object> params) {
        if (zetaApiProp.isShowSql()){
            log.info(sql);
        }
        return namedTemplate.queryForList(sql, params);
    }

    @Override
    public Object modify(Element element, String sql, Map<String, Object> params) {
        if (zetaApiProp.isShowSql()){
            log.info(sql);
        }
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