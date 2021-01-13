package com.heyi.${project.dataBase}.baseservice;

import com.alibaba.fastjson.JSONObject;
import com.heyi.${project.dataBase}.platform.customer.Customer;
import com.heyi.${project.dataBase}.platform.sysuser.SysUser;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class RabbitmqService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final Integer autoReceiptDate = 1123200000;

    //用户创建
    public void sysuserAdd(SysUser sysUser, String mima) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mima", mima);
        jsonObject.put("sysuser", sysUser);
        rabbitTemplate.convertAndSend("${project.dataBase}.sapi", "${project.dataBase}.sysuser.add", jsonObject);
    }


    //忘记密码找回
    public void sysuserPasswordFound(SysUser sysUser, String mima) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mima", mima);
        jsonObject.put("sysuser", sysUser);
        rabbitTemplate.convertAndSend("${project.dataBase}.sapi", "${project.dataBase}.sysuser.foundpassword", jsonObject);
    }

    //s端币变动发消息
    public void customerCoinChange(Customer customer, BigDecimal coin){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("customer", customer);
        jsonObject.put("coin", coin);
        rabbitTemplate.convertAndSend("${project.dataBase}.sapi", "${project.dataBase}.customer.customercoinchange", jsonObject);
    }


//    //提现到账给用户发消息
//    public void customerWithdrawal(Withdrawal withdrawal) {
//        rabbitTemplate.convertAndSend("${project.dataBase}.sapi", "${project.dataBase}.withdrawal.add", withdrawal);
//    }



    //点赞发消息、评论发消息
    public void messageforreviewlike(String message, Long customerId,Integer type,Long suggestionId) {
//        CustomerMessage customerMessage=new CustomerMessage();
//        customerMessage.setCustomerId(customerId);
//        customerMessage.setCustomerMessageType(type);
//        customerMessage.setCustomerMessageContent(message);
//        JSONObject jsonObject=new JSONObject();
//        jsonObject.put("customerMessage",customerMessage);
//        jsonObject.put("suggestionId",suggestionId);
//        rabbitTemplate.convertAndSend("${project.dataBase}.sapi", "${project.dataBase}.system.messageforreviewlike", jsonObject);
    }


}