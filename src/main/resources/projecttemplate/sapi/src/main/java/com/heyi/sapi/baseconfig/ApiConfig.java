package com.heyi.${project.dataBase}.baseconfig;

//所有的url配置都在这里
public class ApiConfig {
    //不用登录的api
    public static final String[] NO_AUTH = {
            "/download",
    };
    //不用解密的api
    public static final String[] NO_DECRYPTION = {
            "reviewCallback",
    };
    //不用加密返回的api
    public static final String[] NO_ENCRYPTION = {
//            swagger
            "/v2/api-docs",
            "/swagger-resources",
            "/wxpay/notify/refund",
            "/wxpay/notify/test"
    };
}
