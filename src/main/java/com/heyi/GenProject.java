package com.heyi;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import freemarker.template.Template;
import lombok.extern.log4j.Log4j;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;
import java.util.List;

import static com.heyi.GenCode.gettableinfo;
import static com.heyi.Utils.isKeyword;

@Log4j
public class GenProject {
    private static List<Table> tables;
    private static Map<String, Object> renderParameters = new HashMap<>();

    private static Project initProject() throws Exception {
        Project project = new Project();
        if (!project.getGenerateFolder() && !project.getGenerateIterate()) {
            throw new Exception("啥也不想干啊");
        }
        if (project.getGenerateIterate()) {
            tables = gettableinfo(project);
        }
        FileUtil.mkdir(project.getTargetPath());
        renderParameters.put("project", project);
        renderParameters.put("tables", tables);
        return project;

    }

    public static void main(String[] args) throws Exception {
        //模板存在的目录
        String projectTemplate = "projecttemplate/";
        Project project = initProject();
        //todo 多个项目一起生成
        String templatest = ResourceUtil.readUtf8Str(projectTemplate + project.getTemplateName() + ".json");
        Template template = new Template(null, templatest, null);
        StringWriter stringWriter = new StringWriter();
        template.process(renderParameters, stringWriter);
        JSONObject templatejson = JSONUtil.parseObj(stringWriter.toString());
        if (project.getGenerateFolder()) {
            generateFolder(templatejson, projectTemplate, project.getTargetPath());
        }
        if (project.getGenerateIterate()) {
            generateIterate(templatejson, projectTemplate, project.getTargetPath());
        }
    }

    //生成框架，不管业务文件夹
    private static void generateFolder(JSONObject templatejson, String templateParentPath, String targetParentPath) {
        Set<String> filenames = templatejson.keySet();
        //遍历同级目录
        for (String filename : filenames) {
            if (isKeyword(filename)) {
                continue;
            }
            JSONObject fileobject = templatejson.getJSONObject(filename);
            if (fileobject.getStr("delete").equals("true")) {
                continue;
            }
            if (fileobject.getBool("iterate")) {
                continue;
            }
            String templatePath = templateParentPath;
            if (!templateParentPath.endsWith("/")) {
                templatePath = templateParentPath + "/";
            }
            templatePath = templatePath + filename;
            String targetPath = null;
            try {
                targetPath = getTargetName(targetParentPath, fileobject, filename);
                //文件
                if (fileobject.getStr("type").equals("file")) {
                    InputStream inputStream = ResourceUtil.getStream(templatePath);
                    String type = FileTypeUtil.getType(inputStream);
                    if (type != null || targetPath.contains(".woff") || targetPath.contains(".woff2")) {
                        FileWriter writer = new FileWriter(targetPath);
                        byte[] bytes = ResourceUtil.readBytes(templatePath);
                        writer.write(bytes, 0, bytes.length);
                    } else {
                        String templatest = ResourceUtil.readUtf8Str(templatePath);
                        FileWriter writer = new FileWriter(targetPath);
                        Template template = new Template(null, templatest, null);
                        template.process(renderParameters, writer.getWriter(false));
                    }
                }
            } catch (Exception e) {
                log.error("文件" + templatePath + "出错" + e.getMessage(), e);
            }
            //文件夹
            if (fileobject.getStr("type").equals("folder")) {
                FileUtil.mkdir(targetPath);
                JSONObject child = templatejson.getJSONObject(filename);
                generateFolder(child, templatePath, targetPath);
            }
        }
    }


    //循环生成业务文件夹
    private static void generateIterate(JSONObject templatejson, String templateParentPath, String targetParentPath) {
        Set<String> filenames = templatejson.keySet();
        for (String filename : filenames) {
            String templatePath = templateParentPath + "/" + filename;
            if (isKeyword(filename)) {
                continue;
            }
            JSONObject fileobject = templatejson.getJSONObject(filename);
            if (fileobject.getStr("delete").equals("true")) {
                continue;
            }
            try {
                //渲染业务文件夹-》数据库表，表循环
                if (fileobject.getStr("type").equals("folder")) {
                    JSONObject child = templatejson.getJSONObject(filename);
                    if (fileobject.getBool("iterate")) {
                        for (Table table : tables) {
                            renderParameters.put("table", table);
                            if (getDeleteString(fileobject).equals("true")) {
                                continue;
                            }
                            String targetPath = getTargetName(targetParentPath, fileobject, table.getTableNameLowerCamel());
                            FileUtil.mkdir(targetPath);
                            generateCode(child, templatePath, targetPath, table);
                        }
                    }
                    if (!fileobject.getBool("iterate")) {
                        String targetPath = getTargetName(targetParentPath, fileobject, filename);
                        generateIterate(child, templatePath, targetPath);
                    }
                }
                //渲染freemark文件,
                if (fileobject.getStr("type").equals("file")) {
                    if (!fileobject.getBool("iterate")) {
                        continue;
                    }
                    for (Table table : tables) {
                        renderParameters.put("table", table);
                        String templatest = ResourceUtil.readUtf8Str(templatePath);
                        String targetPath = getTargetName(targetParentPath, fileobject, table.getTableNameLower());
                        FileWriter writer = new FileWriter(targetPath);
                        Template template = new Template(null, templatest, null);
                        template.process(renderParameters, writer.getWriter(false));
                    }
                }
            } catch (Exception e) {
                log.error("文件" + templatePath + "出错");
                log.error(e.getMessage(), e);
            }
        }
    }

    //生成模板文件
    private static void generateCode(JSONObject templatejson, String parentTemplatePath, String parentTargetPath, Table table) throws Exception {
        Set<String> filenames = templatejson.keySet();
        for (String filename : filenames) {
            String templatePath = parentTemplatePath + "/" + filename;
            if (isKeyword(filename)) {
                continue;
            }
            renderParameters.put("table", table);
            JSONObject fileobject = templatejson.getJSONObject(filename);
            if (fileobject.getStr("delete").equals("true")) {
                continue;
            }
            String targetPath = getTargetName(parentTargetPath, fileobject, filename);
            //文件
            if (fileobject.getStr("type").equals("file")) {
                String templatest = ResourceUtil.readUtf8Str(templatePath);
                FileWriter writer = new FileWriter(targetPath);
                Template template = new Template(null, templatest, null);
                template.process(renderParameters, writer.getWriter(false));
            }
            //文件夹
            if (fileobject.getStr("type").equals("folder")) {
                FileUtil.mkdir(targetPath);
                JSONObject child = templatejson.getJSONObject(filename);
                generateCode(child, templatePath, targetPath, table);
            }
        }
    }

    private static String getTargetName(String parentTargetPath, JSONObject fileobject, String filename) throws Exception {
        if (!fileobject.getStr("rename").equals("")) {
            Template template = new Template(null, fileobject.getStr("rename"), null);
            StringWriter stringWriter = new StringWriter();
            template.process(renderParameters, stringWriter);
            return parentTargetPath + "/" + stringWriter.toString();
        }
        return parentTargetPath + "/" + filename;
    }

    private static String getDeleteString(JSONObject fileobject) throws Exception {
        if (fileobject.getStr("delete").contains("${")) {
            Template template = new Template(null, fileobject.getStr("delete"), null);
            StringWriter stringWriter = new StringWriter();
            template.process(renderParameters, stringWriter);
            return stringWriter.toString();
        }
        return fileobject.getStr("delete");
    }
}
