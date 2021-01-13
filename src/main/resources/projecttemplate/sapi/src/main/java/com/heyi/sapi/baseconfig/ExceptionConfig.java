package com.heyi.${project.dataBase}.baseconfig;

import com.heyi.${project.dataBase}.basecommon.BaseException;
import com.heyi.${project.dataBase}.basecommon.BaseResponse;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.heyi.${project.dataBase}.basecommon.BaseConstant.NOT_LOGIN;


/**
 * @author weibo
 * 拦截全局异常
 */
@RestControllerAdvice
@Slf4j
public class ExceptionConfig {

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public BaseResponse errorHandler(HttpMessageNotReadableException e) {
        return new BaseResponse(false, "传递参数错误");
    }

    @ExceptionHandler(value = Exception.class)
    public BaseResponse errorHandler(Exception e) {
        BaseResponse comm = new BaseResponse(false, e.getMessage());
        if (e instanceof HttpMessageNotReadableException) {
            comm.setDescription("传递参数错误");
            return comm;
        }
        if (e instanceof BaseException) {
            comm.setCode(((BaseException) e).getCode());
            comm.setDescription(e.getMessage());
            return comm;
        }
        if (e instanceof JwtException) {
            comm.setCode(NOT_LOGIN);
            comm.setDescription("请重新登录");
            return comm;
        }
        log.error(e.getMessage(), e);
        return comm;
    }
}