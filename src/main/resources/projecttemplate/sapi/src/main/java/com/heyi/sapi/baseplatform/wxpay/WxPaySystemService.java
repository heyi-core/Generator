package com.heyi.${project.dataBase}.baseplatform.wxPay;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.heyi.${project.dataBase}.baseservice.RabbitmqService;
import com.heyi.${project.dataBase}.baseutil.WXUtils;
import com.heyi.${project.dataBase}.platform.customer.Customer;
import com.heyi.${project.dataBase}.platform.customer.CustomerService;
import com.heyi.${project.dataBase}.platform.product.ProductService;
import com.heyi.${project.dataBase}.platform.productorder.ProductOrder;
import com.heyi.${project.dataBase}.platform.productorder.ProductOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.heyi.${project.dataBase}.basecommon.BaseConstant.*;


@Service
@PropertySource("classpath:/wxpay.properties")
@Slf4j
public class WxPaySystemService {

    @Value("${r'${'}spbillCreateIp}")
    private String spbillCreateIp;
    @Value("${r'${'}notifyUrl}")
    private String notifyUrl;
    @Value("${r'${'}notifyUrl_refund}")
    private String notifyUrl_refund;
    @Value("${r'${'}appId}")
    private String appId;
    @Value("${r'${'}jsapiId}")
    private String jsapiId;

    @Resource
    private WxPayService wxPayService;
    @Resource
    private ProductOrderService productOrderService;
    @Resource
    private RabbitmqService rabbitmqService;
    @Resource
    private CustomerService customerService;
    @Resource
    private ProductService productService;

    /**
     * 微信支付
     *
     * @param productOrder
     * @return
     * @throws WxPayException
     */
    @Transactional
    public String miniAppPay(ProductOrder productOrder, Customer customer) throws WxPayException {
        //拼装微信支付订单信息
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        request.setOutTradeNo(String.valueOf(productOrder.getPayId()));
        request.setBody(StringUtils.isNotBlank(productOrder.getProductName()) ? productOrder.getProductName() : "购物车结算");
        BigDecimal minutePrice = productOrder.getPayPrice().multiply(CARDINALITY_BIGDECIMAL);
        request.setTotalFee(minutePrice.intValue());
        request.setSpbillCreateIp(spbillCreateIp);
        request.setNotifyUrl(notifyUrl);
        if (productOrder.getPayType().equals(WX_APP)) {
            WxPayConfig config = wxPayService.getConfig();
            config.setAppId(appId);
            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(config);
            request.setTradeType(APP_PAY_TYPE);
        } else {
            WxPayConfig config = wxPayService.getConfig();
            config.setAppId(jsapiId);
            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(config);
            request.setTradeType(WX_PAY_TYPE);
            request.setOpenid(customer.getOpenid());
        }
        return JSON.toJSONString(wxPayService.createOrder(request));
    }

    /**
     * 微信支付回调
     * @param xmlData
     * @return
     */
    @Transactional
    public String notify(String xmlData) throws Exception {
        WxPayOrderNotifyResult notifyResult = this.wxPayService.parseOrderNotifyResult(xmlData);
        Map<String, String> xmlToMap = WXUtils.xmlToMap(notifyResult.getXmlString());
        log.warn("支付回调信息  --> " + xmlData);
        String returnCode = notifyResult.getReturnCode();
        if (!"SUCCESS".equalsIgnoreCase(returnCode)) {
            log.error("微信支付回调失败  -->  " + xmlData);
            return WxPayNotifyResponse.success("OK");
        }
        List<ProductOrder> orders = productOrderService.getByPayid(Long.parseLong(notifyResult.getOutTradeNo()));
        for (ProductOrder productOrder : orders) {
            productOrder.setPayInfo(JSON.toJSONString(xmlToMap));
            this.updatePayState(productOrder);
        }
        return WxPayNotifyResponse.success("OK");
    }

    /**
     * 微信退款
     * @param productOrder
     * @return
     */
    public WxPayRefundResult refund(ProductOrder productOrder, Customer customer) throws WxPayException {
        ProductOrder order = productOrderService.getById(productOrder.getOrderId());
        String payInfo = order.getPayInfo();
        JSONObject object = JSON.parseObject(payInfo);
        WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
        wxPayRefundRequest.setNotifyUrl(notifyUrl_refund);
        wxPayRefundRequest.setOutTradeNo(object.getString("out_trade_no"));
        wxPayRefundRequest.setTotalFee(object.getInteger("total_fee"));
        wxPayRefundRequest.setDeviceInfo(customer.getOpenid());
        wxPayRefundRequest.setRefundFee(productOrder.getPayPrice().multiply(CARDINALITY_BIGDECIMAL).intValue());
        wxPayRefundRequest.setOutRefundNo(productOrder.getOrderId().toString());
        return wxPayService.refund(wxPayRefundRequest);
    }

    /**
     * 微信退款回调
     * @param xmlData
     * @return
     * @throws Exception
     */
    public String parseRefundNotifyResult(String xmlData) throws Exception {
        WxPayRefundNotifyResult result = this.wxPayService.parseRefundNotifyResult(xmlData);
        Map<String, String> xmlToMap = WXUtils.xmlToMap(result.getXmlString());
        String returnCode = result.getReturnCode();
        String reqInfo = WXUtils.decryptData(xmlToMap.get("req_info"), wxPayService.getConfig().getMchKey());
        Map<String, String> orderMap = WXUtils.xmlToMap(reqInfo);
        String out_refund_no = orderMap.get("out_refund_no");
        ProductOrder refundOrder = productOrderService.getById(Long.valueOf(out_refund_no));
        if (!"SUCCESS".equalsIgnoreCase(returnCode)) {
            rabbitmqService.refundFaild(refundOrder);
            log.error("微信退款回调失败  -->  " + orderMap);
            return WxPayNotifyResponse.success("OK");
        }
        log.warn("退款回调订单信息  -->  " + orderMap);
        refundOrder.setRefundPrice(refundOrder.getPayPrice());
        refundOrder.setRefundInfo(JSON.toJSONString(orderMap));
        this.updateRefundState(refundOrder);
        return WxPayNotifyResponse.success("OK");
    }

    /**
     * 修改退款状态
     *
     * @param productOrder
     */
    private void updateRefundState(ProductOrder productOrder) {
        //如果状态不是2 return
        if (productOrder.getOrderState() != PENDING_DELIVERY) {
            return;
        }
        productOrder.setOrderState(Integer.valueOf(productOrder.getOrderState() + "4"));
        productOrder.setRefundTime(Calendar.getInstance().getTime());
        productOrderService.update(productOrder, productOrder.getCustomerId());
    }

    /**
     * 修改支付状态
     *
     * @param productOrder
     */
    private void updatePayState(ProductOrder productOrder) {
        if (productOrder.getOrderState() != PENDING_PAYMENT) {
            return;
        }
        productOrder.setPayType("WX");
        productOrder.setOrderState(PENDING_DELIVERY);
        productOrder.setPayTime(Calendar.getInstance().getTime());
        Customer customer = customerService.getById(productOrder.getCustomerId());
        if (productOrder.getCoin() == null) {
            customer.setCoin(COIN);
        } else {
            customerService.addCoin(customer, productOrder.getCoin().negate(), "币消费");
        }
        productOrderService.update(productOrder, productOrder.getCustomerId());
        rabbitmqService.productOrderPay(productOrder);
        //支付成功后增加销量
        productService.saleNumAdd(productOrder.getProductId());
    }


    /*
    * 二维码生成
    * */
    public String wxPayUrl(ProductOrder productOrder) throws Exception {
        HashMap<String, String> data = new HashMap<String, String>();
        //公众账号ID
        data.put("appid",appId);
        //商户号
        data.put("mch_id", machid);
        //随机字符串
        data.put("nonce_str", getNonceStr());
        //商品描述
        data.put("body",productOrder.getProductName());
        //商户订单号
        data.put("out_trade_no",String.valueOf(productOrder.getOrderId()));
        //标价币种
        data.put("fee_type","CNY");
        //标价金额
        data.put("total_fee",String.valueOf(Math.round(productOrder.getPayPrice().doubleValue()*100)));
        //用户的IP
        data.put("spbill_create_ip",spbillCreateIp);
        //通知地址
        data.put("notify_url",notifyUrl);
        //交易类型
        data.put("trade_type","NATIVE");
        //签名类型
        data.put("sign_type","MD5");
        //签名
        data.put("sign", WxUtil.getSignature(data, key,"MD5"));
        String requestXML = WxUtil.mapToXml(data);
        String reponseString =  HttpUtil.post("https://api.mch.weixin.qq.com/pay/unifiedorder",requestXML);
        Map<String,String> resultMap = WxUtil.processResponseXml(reponseString,"MD5");
        if(resultMap.get("return_code").equals("SUCCESS")){
            return resultMap.get("code_url");
        }
        throw new BaseException(FAILED, resultMap.toString());
    }


    /*
    * H5支付
    * */
    public Map<String, String> vipOrderH5Pay(ProductOrder productOrder) throws Exception {
        //返回参数
        Map<String, String> returnMap = new HashMap<>();
        //请求参数封装
        Map<String, String> data = new HashMap<>();
        data.put("appid",appId);
        data.put("mch_id", machid);
        data.put("nonce_str", WxUtil.getNonceStr());
        data.put("body", productOrder.getVipPackageTitle());
        data.put("out_trade_no",String.valueOf(productOrder.getVipOrderId()));//订单号
        data.put("total_fee", String.valueOf(Math.round(productOrder.getPayPrice().doubleValue()*100)));//支付金额
        data.put("spbill_create_ip",spbillCreateIp); //自己的服务器IP地址
        data.put("notify_url", notifyUrl);
        data.put("trade_type", "MWEB");//交易类型
        data.put("sign", WxUtil.getSignature(data, key,"MD5"));
        String requestXML = WxUtil.mapToXml(data);
        String reponseString  =HttpUtil.post("https://api.mch.weixin.qq.com/pay/unifiedorder",requestXML);
        Map<String,String> response = WxUtil.processResponseXml(reponseString,"MD5");
        String returnCode = response.get("return_code");
        if (returnCode.equals("SUCCESS")) {
            returnMap.put("ok", "200");
            //拼接返回跳转地址    //跳转URL  //redirect_url则为支付后返回的指定页面
            String url= URLEncoder.encode("https://www.jinbeijie.com/payResult"+vipOrder.getVipOrderId());
            //拼接返回跳转地址    //跳转URL  //redirect_url则为支付后返回的指定页面
            returnMap.put("url", response.get("mweb_url")+"&redirect_url="+url);
        } else {
            returnMap.put("ok", "201");
            returnMap.put("url",null);
            return returnMap;
        }
        return returnMap;
    }
}
