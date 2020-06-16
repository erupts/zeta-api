package xyz.erupt.openApi.handler;

/**
 * @author liyuepeng
 * @date 2020-06-16
 */
public interface CacheHandler {

    default void put(String key, Long expire) {

    }

    default void get(String key) {

    }

}
