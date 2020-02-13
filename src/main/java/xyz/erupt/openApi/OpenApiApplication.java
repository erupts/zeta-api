package xyz.erupt.openApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author liyuepeng
 * @date 2020-02-13
 */
@SpringBootApplication()
public class OpenApiApplication  extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(OpenApiApplication.class, args);
    }
}
