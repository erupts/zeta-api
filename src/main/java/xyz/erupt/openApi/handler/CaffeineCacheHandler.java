package xyz.erupt.openApi.handler;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author liyuepeng
 * @date 2020-06-16
 */
@Service
public class CaffeineCacheHandler implements CacheHandler {

    private final Map<String, Cache<String, Object>> cacheMap = new ConcurrentHashMap<>();

    private final Gson gson = new Gson();

    @Override
    public void put(String key, Map<String, Object> param, Object value, Long expire) {
        Cache<String, Object> cache = cacheMap.get(key);
        if (null == cache) {
            cache = Caffeine.newBuilder().initialCapacity(1).maximumSize(100)
                    .expireAfterWrite(expire, TimeUnit.MILLISECONDS)
                    .build();

            cacheMap.put(key, cache);
        }
        cache.put(gson.toJson(param), value);
    }

    @Override
    public Object get(String key, Map<String, Object> param) {
        Cache cache = cacheMap.get(key);
        if (null == cache) {
            return null;
        } else {
            return cache.getIfPresent(gson.toJson(param));
        }
    }
}
