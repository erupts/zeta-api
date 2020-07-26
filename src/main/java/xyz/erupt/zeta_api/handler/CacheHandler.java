package xyz.erupt.zeta_api.handler;

import java.util.Map;

/**
 * @author liyuepeng
 * @date 2020-06-16
 */
public interface CacheHandler {

    void put(String key, Map<String, Object> param, Object value, Long expire);

    Object get(String key, Map<String, Object> param);

}
