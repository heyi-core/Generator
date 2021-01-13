package com.heyi;

import lombok.Data;

@Data
public class Colum {
    /*注解*/
    private String columnComment;
    /*sqlType*/
    private String sqlType;
    /*jdbcType*/
    private String jdbcType;
    /*javaType*/
    private String javaType;
    /*importType*/
    private String importType;
    /*esType*/
    private String esType;
    /*字段名称*/
    private String columnName;
    /*小写没有下划线*/
    private String columnNameLower;
    /*小写驼峰*/
    private String columnNameLowerCamel;
    /*大写驼峰*/
    private String columnNameUpperCamel;
}

