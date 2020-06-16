package xyz.erupt.openApi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author liyuepeng
 * @date 2019-10-31.
 */
@Data
@Component
@ConfigurationProperties(prefix = "open-api")
public class OpenApiConfig {

    private boolean hotReadXml = false;

    private boolean openCache = true;

    private boolean apiDoc = false;

    private String xmlbasePath = "/epi";

    private String cacheProvider = "xyz.erupt.openApi.handler.CaffeineCacheHandler";

}
