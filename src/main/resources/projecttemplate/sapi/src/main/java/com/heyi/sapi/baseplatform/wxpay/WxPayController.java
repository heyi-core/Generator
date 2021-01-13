package com.heyi.${project.dataBase}.baseplatform.wxPay;

import com.heyi.${project.dataBase}.basecommon.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;


/**
 * @author Binary Wang
 */
@Api(value = "微信支付", tags = "微信支付接口")
@RestController
@RequestMapping("/wxpay")
@Slf4j
public class WxPayController extends BaseController {
    @Resource
    private WxPaySystemService wxPaySystemService;

    @ApiOperation(value = "支付回调通知处理")
    @PostMapping("/notify/order")
    public String parseOrderNotifyResult(@RequestBody String xmlData) throws Exception {
        String notify = wxPaySystemService.notify(xmlData);
        log.warn("微信支付回调成功返回的信息 -->  "+notify);
        return notify;
    }

    @ApiOperation(value = "退款回调通知处理")
    @PostMapping("/notify/refund")
    public String parseRefundNotifyResult(@RequestBody String xmlData) throws Exception {
        String s = wxPaySystemService.parseRefundNotifyResult(xmlData);
        log.warn("微信退款回调成功返回的信息  -->  "+s);
        return s;
    }

    /**
     * 微信二维码
     */
    @PostMapping(value = {"/payUrl"})
    public BaseResponse payUrl(@RequestBody ProductOrder productOrder) throws Exception{
        String codeurl=wxPaySystemService.wxPayUrl(productOrder);
        return new BaseResponse(true,"生成成功",codeurl);
    }

    /**
     * 微信H5支付
     */
    @PostMapping(value = {"/vipOrderH5Pay"})
    public BaseResponse vipOrderH5Pay(@RequestBody ProductOrder productOrder) throws Exception {
        Map<String, String> codeurl= wxPaySystemService.vipOrderH5Pay(productOrder);
        return new BaseResponse(true,"生成成功",codeurl);
    }
}

