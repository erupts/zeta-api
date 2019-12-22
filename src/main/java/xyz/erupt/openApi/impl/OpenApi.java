package xyz.erupt.openApi.impl;

import org.dom4j.Element;

import java.util.Map;

/**
 * @author liyuepeng
 * @date 2019-12-22
 */
public interface OpenApi {

    Object query(Element element, String queryStr, Map<String, String> params);

}