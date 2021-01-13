package com.heyi.${project.dataBase}.baseconfig;

import com.heyi.${project.dataBase}.baseutil.PasswordUtil;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;

import static com.heyi.${project.dataBase}.baseconfig.ApiConfig.NO_DECRYPTION;

/**
 * 解密配置
 *
 * @author weibo
 */
@ControllerAdvice
public class DecryptionConfig implements RequestBodyAdvice {

    @Value("${r'${'}open.encrypt}")
    private Boolean openEncrypt;
    @Value("${r'${'}encrypt.key}")
    protected String encryptKey;



    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead( HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return new HttpInputMessage() {
            @SneakyThrows
            @Override
            public InputStream getBody()  {
                String content = IOUtils.toString(httpInputMessage.getBody());
                String path=methodParameter.getMethod().getName();
                if (ArrayUtils.contains(NO_DECRYPTION, path)) {
                    return new ByteArrayInputStream(content.getBytes());
                }
                if (openEncrypt) {
                    return new ByteArrayInputStream(PasswordUtil.desEncrypt(content, encryptKey, encryptKey).getBytes());
                } else {
                    return new ByteArrayInputStream(content.getBytes());
                }
            }

            @Override
            public HttpHeaders getHeaders() {
                return httpInputMessage.getHeaders();
            }
        };
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }




}