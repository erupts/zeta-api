package xyz.erupt.openApi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author liyuepeng
 * @date 2020-06-28
 */
@Data
@Component
@ConfigurationProperties(prefix = "open-doc")
public class ApiDocConfig {

    private boolean enable = false;

    private List<String> ipWhite;
}
