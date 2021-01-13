package com.heyi.${project.dataBase}.basecommon;

import java.math.BigDecimal;

public class BaseConstant {
    //普通异常码
    public static final int FAILED = 999999;
    //无权限
    public static final int PERMISSION_DENIED = 4001;
    //未登录
    public static final int NOT_LOGIN = 4002;
    //删除状态
    public static final int DELETED=1; //删除
    public static final int NOT_DELETED=2;//未删除
    //默认客服
    public static final Long SALEPERSON=1001L;//正常
    //初始数量
    public static final int QUANTITY=0;//0
    //日期格式
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    //请求类型
    public static final String POST = "POST";
    //access_token全局储存时间
    public static final int ACCESS_TOKEN_TIME = 1200;
    //微信价格基数
    public static final BigDecimal CARDINALITY_BIGDECIMAL = new BigDecimal(100);
    //支付类型
    public static final String ALI_PAY="ALIPAY";//支付宝
    public static final String WX_APP="WXAPP";//APP微信支付
    //订单状态
    public static final int PENDING_PAYMENT=1;//待付款
    public static final int PENDING_DELIVERY=2;//代发货
    public static final int SIGN_FOR=4;//已签收

    public static final int GEOCODE_LEN=10;//精确度 最大12位
    //IM创建
    public static final int IM_CREATE=1;
    public static final int IM_UPDATEUINFO=2;
    public static final int IM_UPDATE=3;




    //VIP
    public static final int NO_VIP=1;//普通
    //用户状态
    public static final int NORMAL=1;//正常
    //默认父级
    public static final Long PARENT=2001L;//正常
    //初始妮妮币
    public static final BigDecimal COIN= new BigDecimal(0.00);
    //初始金额
    public static final BigDecimal DEFAULT_PRICE= new BigDecimal(0.00);
    //初始数量
    public static final int DEFAULT_LIKE_NUM=0;
    public static final int LIKE_NUM=1;
    public static final int COMMENT_NUM=0;
    public static final int SHARE_NUM=0;
    public static final int VISIT_NUM=0;

    //微信支付类型
    public static final String APP_PAY_TYPE="APP";//app
    public static final String WX_PAY_TYPE="JSAPI";//app
    public static final String WX_PAY_NATIVE="NATIVE";//扫码
    public static final String WX_PAY_MWAP="MWEB";//扫码
    // 待确认
    public static final Integer SUGGESTION_UNCONFIRMED=1;//APP微信支付
    //消息类型 7评论
    public static final Integer MESSAGE_TYPE_COMMENT=7;
    //消息类型 8关注
    public static final Integer MESSAGE_TYPE_FOLLOW=8;
    //消息类型 9赞
    public static final Integer MESSAGE_TYPE_LIKE=9;
    //消息类型 10回答
    public static final Integer MESSAGE_TYPE_ANSWER=10;
    //默认分数
    public static final Integer DEFAULT_SCORE=1;
    public static final Long DEFAULT_REDIS_EXIST_TIME=86400L;

    //腾讯云超时时间
    public static final Long EXPIRE_TIME = 60*60*24*7L;
    //feed的名称
    public static final String FEED_KEY = "${project.dataBase}";
    //feed eat的名称
    public static final String EAT_KEY = "${project.dataBase}eat";
    //eat的初始值
    public static final Integer DEFAULT_EAT = 0;
    //长度的初始值
    public static final Integer DEFAULT_LENGTH = 0;
    //默认坐标
    public static final Double DEFAULT_COO = 0.0;
    //默认数量
    public static final Integer DEFAULT_NUM = 1;
    //默认减值
    public static final Integer DEFAULT_CUT = -1;
    //关注数量
    public static final Integer FLLOW_NUM=1;
    //随机数生成长度 10
    public static final Integer RANDOMLENGTH_TEN=10;
}