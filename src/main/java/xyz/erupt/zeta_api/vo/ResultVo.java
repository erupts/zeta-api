package xyz.erupt.zeta_api.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liyuepeng
 * @date 2021/1/13 17:26
 */
@Getter
@Setter
public class ResultVo {

    private Object result;

    private boolean success;

    private Map<String, Object> links = new HashMap<>();

}
