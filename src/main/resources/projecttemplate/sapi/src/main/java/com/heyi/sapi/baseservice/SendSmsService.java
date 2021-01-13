package com.heyi.${project.dataBase}.baseservice;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.heyi.${project.dataBase}.basecommon.BaseException;
import com.heyi.${project.dataBase}.platform.customer.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;

import static com.heyi.${project.dataBase}.basecommon.BaseConstant.FAILED;

@Service
@PropertySource("classpath:sms.properties")
public class SendSmsService {
    @Value("${r'${'}sms.signName}")
    private String signName;
    @Value("${r'${'}sms.templateCode}")
    private String templateCode;
    @Value("${r'${'}sms.regionId}")
    private String regionId;
    @Value("${r'${'}sms.accessKeyId}")
    private String accessKeyId;
    @Value("${r'${'}sms.secret}")
    private String secret;
    @Value("${r'${'}sms.domain}")
    private String domain;
    @Value("${r'${'}sms.version}")
    private String version;
    @Value("${r'${'}sms.action}")
    private String action;

    @Resource
    private RedisService redisService;

    /*发送验证码*/
    public Boolean sendSms(String phone, String code) throws Exception {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, secret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(domain);
        request.setVersion(version);
        request.setAction(action);
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", "{\"code\":" + code + "}");
        CommonResponse response = client.getCommonResponse(request);
        String message = response.getData();
        message = message.substring(12,14);
        if (message.equals("OK")) {
            return true;
        } else {
            return false;
        }
    }

    /***
     * 获取六位数手机验证码
     */
    public String getVerificationCode() {
        Random ra = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            stringBuilder.append(ra.nextInt(9) + 1);
        }
        return stringBuilder.toString();
    }
}