package com.heyi.${project.dataBase}.baseutil;

import com.alibaba.fastjson.JSON;
import com.heyi.${project.dataBase}.platform.sysuser.SysUser;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

// 专门用于收集操作日志
@Slf4j
public class LogUtil {
    //    不需要存放对象比如查询分页
    public static void info(SysUser sysUser, String url, String describe) {
        Map<String, Object> map = new HashMap<>();
        map.put("sysUser", sysUser);
        map.put("url", url);
        map.put("describe", describe);
        log.warn(JSON.toJSONString(map));
    }

    //    需要存放对象比如增加，删除
    public static void info(SysUser sysUser, String url, String describe, Object object) {
        Map<String, Object> map = new HashMap<>();
        map.put("sysUser", sysUser);
        map.put("url", url);
        map.put("describe", describe);
        map.put("object", object);
        log.warn(JSON.toJSONString(map));
    }

    //    针对修改内容，需要传入oldobject修改前的内容，newObject修改后的内容
    public static void info(SysUser sysUser, String url, String describe, Object oldObject, Object newObject) {
        Map<String, Object> map = new HashMap<>();
        map.put("sysUser", sysUser);
        map.put("url", url);
        map.put("describe", describe);
        map.put("newObject", newObject);
        map.put("oldObject", oldObject);
        log.warn(JSON.toJSONString(map));
    }
}
