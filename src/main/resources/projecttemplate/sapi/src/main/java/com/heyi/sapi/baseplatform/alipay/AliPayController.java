package com.heyi.${project.dataBase}.baseplatform.alipay;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 支付宝
 * @author lcc
 * @data :2018年6月4日 上午10:55:46
 */
@RestController
@Scope("request")
@RequestMapping("/alipay")
@Api(value = "支付宝支付", tags = "支付宝支付")
public class AliPayController {

    @Autowired
    private AliPayService alipayService;

    @ApiOperation(value = "支付结果回调", notes = "支付结果回调")
    @PostMapping(value = "/notifyUrl")
    public String notifyUrl(HttpServletRequest request) throws Exception {
        String result= alipayService.notifyUrl(request);
        return result;
    }
}

