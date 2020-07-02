package xyz.erupt.openApi.util;

import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * @author liyuepeng
 * @date 2019-01-24
 */
@Component
public class OpenApiSpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (OpenApiSpringUtil.applicationContext == null) {
            OpenApiSpringUtil.applicationContext = applicationContext;
        }
    }

    private static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @SneakyThrows
    public static <T> T getBean(Class<T> clazz) {
        if (null != clazz.getDeclaredAnnotation(Component.class)
                || null != clazz.getDeclaredAnnotation(Service.class)
                || null != clazz.getDeclaredAnnotation(Repository.class)
                || null != clazz.getDeclaredAnnotation(Controller.class)) {
            return getApplicationContext().getBean(clazz);
        } else {
            return clazz.newInstance();
        }
    }

    //根据类路径获取bean
    public static <T> T getBeanByPath(String path, Class<T> clazz) throws ClassNotFoundException {
        return clazz.cast(getBean(Class.forName(path)));
    }
}