package com.heyi.${project.dataBase}.baseutil;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class AlipayUitl {
    // 获取支付宝POST过来反馈信息
    public static Map<String, String> toMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }

    //获取文件绝对路径
    public static  String  getPathUrl(String filePath, String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        File certFile=new File(filePath+"\\"+resource.getPath());
        if(certFile.exists()){
            return certFile.getPath();
        }
        if(!certFile.exists() ){
            FileUtil.touch(certFile);
        }
        InputStream in = resource.getStream();
        OutputStream out  = new FileOutputStream(certFile);
        IoUtil.copy(in, out, IoUtil.DEFAULT_BUFFER_SIZE);
        return  certFile.getPath();
    }
}
