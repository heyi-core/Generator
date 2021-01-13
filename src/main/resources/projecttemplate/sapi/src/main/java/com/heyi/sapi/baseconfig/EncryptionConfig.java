package com.heyi.${project.dataBase}.baseconfig;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.heyi.${project.dataBase}.baseutil.PasswordUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import static com.heyi.${project.dataBase}.baseconfig.ApiConfig.NO_ENCRYPTION;


/**
 * 加密
 *
 * @author weibo
 */
@ControllerAdvice
public class EncryptionConfig implements ResponseBodyAdvice {

    @Value("${r'${'}open.encrypt}")
    private Boolean openEncrypt;
    @Value("${r'${'}encrypt.key}")
    private String encryptKey;


    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        String path = serverHttpRequest.getURI().getPath();
        if (openEncrypt && !ArrayUtil.contains(NO_ENCRYPTION, path)) {
            return PasswordUtil.encrypt(JSON.toJSONString(o), encryptKey, encryptKey);
        } else {
            return o;
        }
    }
}