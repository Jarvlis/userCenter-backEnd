package com.jarvlis.usercenter.exception;

import com.jarvlis.usercenter.common.BaseResponse;
import com.jarvlis.usercenter.common.ErrorCode;
import com.jarvlis.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author shisi
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BussinessException.class)
    public BaseResponse busniessExceptionHandler(BussinessException e){
        log.error("BussinessException: " + e.getMessage() + e);
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e){
        log.error("runtimeException: ", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");
    }
}
