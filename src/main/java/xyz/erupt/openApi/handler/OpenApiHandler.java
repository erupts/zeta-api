package xyz.erupt.openApi.handler;

import org.dom4j.Element;


/**
 * Created by liyuepeng on 2019-08-15.
 */
public interface OpenApiHandler {

    String handler(Element element, String sql);

    default Object handlerResult(Element element, Object result) {
        return result;
    }
}
