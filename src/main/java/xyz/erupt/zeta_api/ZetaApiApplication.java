package xyz.erupt.zeta_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author liyuepeng
 * @date 2020-02-13
 */
@SpringBootApplication()
public class ZetaApiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ZetaApiApplication.class, args);
    }
}
