package com.charli.wcpay.exception;

import com.charli.wcpay.domain.JsonData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常处理控制器
 */
@ControllerAdvice
public class ClExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonData Handler(Exception e){

        if (e instanceof ClException){
            ClException clException = (ClException)e;
            return JsonData.buildError(clException.getMsg(), clException.getCode());
        }else {
            return JsonData.buildError("全局异常，未知错误");
        }
    }
}
