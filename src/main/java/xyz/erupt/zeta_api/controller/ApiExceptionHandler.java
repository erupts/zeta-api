package xyz.erupt.zeta_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import xyz.erupt.zeta_api.util.NotFountException;

/**
 * @author liyuepeng
 * @date 2020-04-10
 */
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NotFountException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleInvalidRequestError(NotFountException e) {
        return "<h1 style='text-align:center'>" + e.getMessage() + "</h1>";
    }

}
