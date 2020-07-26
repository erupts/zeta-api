package xyz.erupt.zeta_api.impl;

import org.dom4j.Element;

import java.util.Collection;
import java.util.Map;

/**
 * @author liyuepeng
 * @date 2019-12-22
 */
public interface ZetaApi {

    Collection query(Element element, String queryStr, Map<String, Object> params);

    Object modify(Element element, String queryStr, Map<String, Object> params);

}