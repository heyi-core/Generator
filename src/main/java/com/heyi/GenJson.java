package com.heyi;

import cn.hutool.core.io.file.FileWriter;
import cn.hutool.json.JSONUtil;
import cn.hutool.system.oshi.OshiUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class GenJson {
    public static void main(String[] args) throws IOException {
        HashMap<String, Object> fileMap = new HashMap<String, Object>();
//        修改这个名称
        String target = "./src/main/resources/projecttemplate/sapi";
        File targetf = new File(target);
        dir2map(targetf, fileMap);
        String jsonf=targetf.getParentFile().getPath() + "/" + targetf.getName() + ".json";
        File targetjson = new File(jsonf);
        if(!targetjson.exists()){
            targetjson.createNewFile();
        }
        FileWriter writer = new FileWriter(targetjson);
        writer.write(JSONUtil.parseFromMap(fileMap).toStringPretty());
    }

    public static void dir2map(File node, HashMap<String, Object> fileMap) {
        HashMap<String, Object> properMapsubDir = new HashMap<String, Object>();
        properMapsubDir.put("delete", "false");
        properMapsubDir.put("rename", "");
        if (node.getName().equals("templete")||node.getName().equals("mappers")){
            properMapsubDir.put("iterate", true);
        }else {
            properMapsubDir.put("iterate", false);
        }
        //是文件，保存文件名和最后修改时间戳
        if (node.isFile()) {
            properMapsubDir.put("type", "file");
            fileMap.put(node.getName(), properMapsubDir);
        }
        //是目录，建立下一层map，并填充
        if (node.isDirectory()) {
            properMapsubDir.put("type", "folder");
            fileMap.put(node.getName(), properMapsubDir);
            for (String filename : node.list()) {
                dir2map(new File(node, filename), properMapsubDir);//填充
            }
        }
    }

}
