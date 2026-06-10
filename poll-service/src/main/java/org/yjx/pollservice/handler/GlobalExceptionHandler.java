package org.yjx.pollservice.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.yjx.pollpojo.exception.BusinessException;
import org.yjx.pollpojo.result.Result;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handle(BusinessException ex){
        return Result.error(ex.getCode(),ex.getMessage());
    }

   // @ExceptionHandler(Exception.class)
    //public Result<Void> handleException(Exception e) {
    //    return Result.error(500, "服务器内部错误");
    //}

}
