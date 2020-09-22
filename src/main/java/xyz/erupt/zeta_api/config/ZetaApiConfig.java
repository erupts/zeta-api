package xyz.erupt.zeta_api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author liyuepeng
 * @date 2019-10-31.
 */
@Data
@Component
@ConfigurationProperties(prefix = "zeta-api")
public class ZetaApiConfig {

    private boolean hotReadXml = false;

    private boolean enableCache = true;

    private boolean enableApiDoc = false;

    private String domain;

    private List<String> ipWhite;

    private String xmlbasePath = "/epi";

    private boolean showSql = true;

    private String cacheHandlerPath = "xyz.erupt.zeta_api.handler.CaffeineCacheHandler";

}
