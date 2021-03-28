package xyz.erupt.zeta_api.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author liyuepeng
 * @date 2021/1/13 16:58
 */
@Configuration
@ComponentScan("xyz.erupt.zeta_api")
@EnableConfigurationProperties({ ZetaApiProp.class })
public class ZetaApiConfig {
}
