package com.heyi.${project.dataBase}.baseplatform.alipay;


import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.heyi.${project.dataBase}.basecommon.BaseException;
import com.heyi.${project.dataBase}.baseservice.RabbitmqService;
import com.heyi.${project.dataBase}.platform.customer.Customer;
import com.heyi.${project.dataBase}.platform.customer.CustomerService;
import com.heyi.${project.dataBase}.platform.product.ProductService;
import com.heyi.${project.dataBase}.platform.productorder.ProductOrder;
import com.heyi.${project.dataBase}.platform.productorder.ProductOrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static com.heyi.${project.dataBase}.basecommon.BaseConstant.*;


/**
 * 支付实现类
 *
 * @author lcc
 * @data :2018年6月4日 上午10:18:07
 */
@PropertySource("classpath:/alipay.properties")
@Service
public class AliPayService {
    @Resource
    private ProductOrderService productOrderService;
    @Resource
    private RabbitmqService rabbitmqService;
    @Resource
    private CustomerService customerService;
    @Resource
    private ProductService productService;
    @Value("${r'${'}alipay.appId}")
    public String APP_ID;
    @Value("${r'${'}alipay.privateKey}")
    public String MERCHANT_PRIVATE_KEY;
    @Value("${r'${'}alipay.publicKey}")
    public String ALIPAY_PUBLIC_KEY;
    @Value("${r'${'}alipay.notifyurl}")
    public String notify_url;
    public String SIGN_TYPE = "RSA2";
    public String CHARSET = "utf-8";
    @Value("${r'${'}alipay.serverUrl}")
    public String GATEWAYURL;
    @Value("${r'${'}alipay.payCertPath}")
    private String payCertPath;
    @Value("${r'${'}alipay.payRootCertPath}")
    private String payRootCertPath;
    @Value("${r'${'}alipay.payAppCertPath}")
    private String payAppCertPath;
    @Value("${r'${'}alipay.filepath}")
    private String filePath;
    @Value("${r'${'}alipay.timeoutExpress}")
    private String timeoutExpress;
    @Value("${r'${'}alipay.productCode}")
    private String productCode;

    /*
     * 退款
     * */
    public String refund(ProductOrder productOrder) throws Exception {
        AlipayClient alipayClients = getAlipayClient();
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
        /** 调取接口*/
        alipayRequest.setBizContent("{\"out_trade_no\":\"" + productOrder.getPayId() + "\","
                + "\"refund_amount\":\"" + productOrder.getPayPrice() + "\","
                + "\"refund_reason\":\"" + productOrder.getRefundReason() + "\","
                + "\"out_request_no\":\"" + productOrder.getOrderId() + "\"}");
        String result = alipayClients.certificateExecute(alipayRequest).getBody();
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject refundResponse = (JSONObject) jsonObject.get("alipay_trade_refund_response");
        String success = refundResponse.get("msg").toString();
        if (success.equals("Success")) {
            productOrder.setRefundPrice(productOrder.getPayPrice());
            productOrder.setRefundInfo(result);
            productOrder.setOrderState(Integer.valueOf(productOrder.getOrderState() + "4"));
            productOrder.setRefundTime(Calendar.getInstance().getTime());
            productOrderService.update(productOrder, productOrder.getCustomerId());
            rabbitmqService.capirefundOrder(productOrder);
        }
        if(success.contains("Failed")){
            productOrder.setPayType("支付宝");
            rabbitmqService.refundFaild(productOrder);
            throw new BaseException(FAILED,"退款失败请重试");
        }
        return "success";
    }

    /*
     * 异步回调
     * */
    public String notifyUrl(HttpServletRequest request) throws Exception {
        // 获取支付宝POST过来反馈信息
        Map<String, String> params = AlipayUitl.toMap(request);
        System.out.println(params);
        boolean verifyResult = AlipaySignature.rsaCertCheckV1(params,  AlipayUitl.getPathUrl(filePath, payCertPath), "UTF-8", "RSA2");
        String states = params.get("trade_status");
        if ("TRADE_CLOSED".equals(states)) {
            //退款回调直接返回success
            return "success";
        }
        if (!verifyResult) {
            return "failure";
        }
        List<ProductOrder> orders = productOrderService.getByPayid(Long.parseLong(params.get("out_trade_no")));
        for (ProductOrder order : orders) {
            order.setPayInfo(params.toString());
            updatePayState(order);
        }
        return "success";
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
        productOrder.setPayType(ALI_PAY);
        productOrder.setOrderState(PENDING_DELIVERY);
        productOrder.setPayTime(Calendar.getInstance().getTime());
        Customer customer = customerService.getById(productOrder.getCustomerId());
        if (productOrder.getCoin() == null) {
            customer.setCoin(COIN);
        } else {
            customerService.addCoin(customer, productOrder.getCoin().negate(), "妮妮币消费");
        }
        productOrderService.update(productOrder, productOrder.getCustomerId());
        rabbitmqService.productOrderPay(productOrder);
        //支付成功后增加销量
        productService.saleNumAdd(productOrder.getProductId());
    }

    /*
     * 获取apipayClient
     * */
    public AlipayClient getAlipayClient() throws Exception {
        String aliPayCertPath = AlipayUitl.getPathUrl(filePath, payCertPath);
        String aliPayRootCertPath = AlipayUitl.getPathUrl(filePath, payRootCertPath);
        String appCertPath = AlipayUitl.getPathUrl(filePath, payAppCertPath);
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        //设置网关地址
        certAlipayRequest.setServerUrl(GATEWAYURL);
        //设置应用Id
        certAlipayRequest.setAppId(APP_ID);
        //设置应用私钥
        certAlipayRequest.setPrivateKey(MERCHANT_PRIVATE_KEY);
        //设置请求格式，固定值json
        certAlipayRequest.setFormat("json");
        //设置字符集
        certAlipayRequest.setCharset(CHARSET);
        //设置签名类型
        certAlipayRequest.setSignType(SIGN_TYPE);
        //设置应用公钥证书路径
        certAlipayRequest.setCertPath(appCertPath);
        //设置支付宝公钥证书路径
        certAlipayRequest.setAlipayPublicCertPath(aliPayCertPath);
        //设置支付宝根证书路径
        certAlipayRequest.setRootCertPath(aliPayRootCertPath);
        //构造client
        AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
        return alipayClient;
    }
}
