package xyz.erupt.openApi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import xyz.erupt.openApi.util.NotFountException;

/**
 * @author liyuepeng
 * @date 2020-04-10
 */
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NotFountException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleInvalidRequestError() {
        return "404 not found";
    }

}
