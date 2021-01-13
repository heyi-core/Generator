package com.heyi.${project.dataBase}.baseutil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * @author beyond
 * @date 2016/11/26
 */
@Component
public class SnowFlakeId {

    private static long START_STMP = 1567662227000L;
    //起始的时间戳,系统上线之前需要确定最终的id数字
    private static long DATACENTER_ID = 0L;
    //数据中心，0表示没有，最大值不能超多MAX_DATACENTER_NUM
    private static long MACHINE_ID = 1L;
    //机器标识，1表示第一个，最大值不能超过MAX_MACHINE_NUM
    private static long SEQUENCE = 0L;
    //序列号
    private static long LAST_STMP = -1L;
    //上一次时间戳

    @Value("${r'${'}snowflake.datacenterId}")
    public void setDatacenterId(String datacenterId) {
        DATACENTER_ID = Long.parseLong(datacenterId);
        if (DATACENTER_ID > MAX_DATACENTER_NUM || DATACENTER_ID < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than " + MAX_DATACENTER_NUM + " or less than 0");
        }
    }

    @Value("${r'${'}snowflake.machineId}")
    public void setMachineId(String machineId) {
        MACHINE_ID = Long.parseLong(machineId);
        if (MACHINE_ID > MAX_MACHINE_NUM || MACHINE_ID < 0) {
            throw new IllegalArgumentException("machineId can't be greater than " + MAX_MACHINE_NUM + "  or less than 0");
        }
    }

    @Value("${r'${'}snowflake.startStmp}")
    public void setStartStmp(String startStmp) {
        START_STMP = Long.parseLong(startStmp);
    }


    //每一部分占用的位数
    private final static long SEQUENCE_BIT = 12;
    //序列号占用的位数
    private final static long MACHINE_BIT = 5;
    //机器标识占用的位数
    private final static long DATACENTER_BIT = 1;
    //数据中心占用的位数

    //每一部分的最大值
    private final static long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

    //每一部分向左的位移
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    //规则
    private static Pattern pattern = Pattern.compile(".*\\d+.*");

    //产生下一个ID
    public static synchronized long generateID() {
        long currStmp = System.currentTimeMillis();
        if (currStmp < LAST_STMP) {
            throw new RuntimeException("系统时间回退了，请修正时间");
        }

        if (currStmp == LAST_STMP) {
            //相同毫秒内，序列号自增
            SEQUENCE = (SEQUENCE + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (SEQUENCE == 0L) {
                currStmp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            SEQUENCE = 0L;
        }

        LAST_STMP = currStmp;

        return (currStmp - START_STMP) << TIMESTMP_LEFT
                //时间戳部分
                | DATACENTER_ID << DATACENTER_LEFT
                //数据中心部分
                | MACHINE_ID << MACHINE_LEFT
                //机器标识部分
                | SEQUENCE;
        //序列号部分
    }

    private static long getNextMill() {
        long mill = System.currentTimeMillis();
        while (mill <= LAST_STMP) {
            mill = System.currentTimeMillis();
        }
        return mill;
    }

    /**
     * 获取六位随机数
     * @return
     */
    public static int get5Math(){
        return (int)((Math.random()*9+1)*100000);
    }

}