package com.heyi.${project.dataBase}.basecommon;

<#if  project.login == true >
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.heyi.${project.dataBase}.platform.sysuser.SysUser;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.heyi.${project.dataBase}.basecommon.BaseConstant.NOT_LOGIN;
import static com.heyi.${project.dataBase}.basecommon.BaseConstant.SALEPERSON;
import static com.heyi.${project.dataBase}.baseconfig.ApiConfig.NO_AUTH;
import static com.heyi.${project.dataBase}.baseutil.PasswordUtil.parseJWT;
import static com.heyi.${project.dataBase}.baseutil.IpUtil.getIpAddress;


@PropertySource("classpath:secret.properties")
public class BaseController {

    public HttpServletRequest request;
    private HttpServletResponse response;
    public SysUser sysUser;

    @Value("${r'${'}token.key}")
    private String tokenKey;
    @Value("${r'${'}open.token}")
    private Boolean openToken;


    /*每个请求都在这里验证*/
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        processAuth(request);
    }

    /*验证登录和权限*/
    private void processAuth(HttpServletRequest request) {
        String path = request.getContextPath() + request.getServletPath();
        //        设置默认id
        sysUser = new SysUser();
        sysUser.setSysUserId(SALEPERSON);
        String ip = getIpAddress(request);
        sysUser.setIp(ip);
        //      不开启登录验证
        if (!openToken) {
            return;
        }
        //       不用登录url
        if (ArrayUtil.contains(NO_AUTH, path)) {
            return;
        }
        //        必须登录
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.isBlank(authHeader)) {
            throw new BaseException(NOT_LOGIN, "请重新登录");
        }
        Claims claims = parseJWT(authHeader, tokenKey);
        sysUser = JSON.parseObject(claims.getSubject(), SysUser.class);

    }
}
</#if>

<#if  project.login == false >
import com.heyi.${project.dataBase}.platform.customer.Customer;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {
    public HttpServletRequest request;
    private HttpServletResponse response;
    public Customer customer;
    /*每个请求都在这里验证*/
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        sysUser = new SysUser();
        sysUser.setSysUserId(SALEPERSON);
    }
}
</#if>
