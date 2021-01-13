package com.heyi;

import cn.hutool.core.date.DateUtil;
import lombok.Data;

@Data
public class Project {
    /*模板项目*/
    private String templateName = "sapi";
    /*项目中文名称*/
    private String artifactName = "管理端接口";
    /*表*/
    private String table = "*";
    /*目标路径*/
    private String targetPath = "d:/java_workspace/test";
    /*是否重新生成项目*/
    private Boolean generateFolder = true;
    /*是否生成业务*/
    private Boolean generateIterate = true;
    /*生成时间*/
    private String createTime = DateUtil.now();
    /*数据库*/
    private String dataBase = "shujukmingcheng";
    /*作者*/
    private String author = "张小龙";
    /*数据库*/
    private String jdbcUrl = "jdbc:mysql://192.168.1.125:3306/shujukmingcheng?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";
    private String jdbcUsername = "shujukmingcheng";
    private String jdbcPassword = "shujukmingcheng";
    private String port = "8080";
    /*开启rabbitmq*/
    private Boolean rabbitmq = true;
    /*登录功能*/
    private Boolean login = true;
    /*加密解密功能*/
    private Boolean encrypt = true;
    /*微信支付功能*/
    private Boolean wxPay = false;
    /*aliPay支付功能*/
    private Boolean aliPay = false;
    /*elk日志功能*/
    private Boolean log = true;
    /*oss上传功能*/
    private Boolean oss = true;
    /*短信功能*/
    private Boolean sms = true;
    /*ios登录*/
    private Boolean iosLogin = true;
    /*微信小程序登录*/
    private Boolean miniLogin = false;
    /*feed热度*/
    private Boolean feed = true;
    /*Im即时通讯*/
    private Boolean im = true;
    /* -----------------------  system项目配置文件  -------------------------------------- */
    /*system端口号*/
    private String systemPort = "8081";
    /*system后台服务器地址*/
    private String portHost = "http://192.168.1.125";
    /*开启oss打包上传功能*/
    private Boolean ossBuild = false;
    /*开启系统管理模块*/
    private Boolean openSystem = false;
    /*开启字典模块，如果开启系统管理关闭也会生成字典管理*/
    private Boolean openDictionary = false;
}
