package com.heyi;

import lombok.Data;

import java.util.List;

@Data
public class Table {
    /*表名称*/
    private String tableName;
    /*表注解*/
    private String tableComment;
    /*表名称小写*/
    private String tableNameLower;
    /*表名称小写驼峰*/
    private String tableNameLowerCamel;
    /*表名称大写驼峰*/
    private String tableNameUpperCamel;
    /*包名称*/
    private String tablePackage;
    /*columns*/
    private List<Colum> columns;
    /*key*/
    private Colum key;
    /*特殊用途*/
    private Boolean haveCreator = false;
    private Boolean haveDeleteState = false;
    /*字段注释包含经度或维度的时候*/
    private Boolean haveLocation = false;
    private Boolean haveCreateTime = false;
    private Boolean haveModifyor = false;
    private Boolean haveModifyTime = false;
}
