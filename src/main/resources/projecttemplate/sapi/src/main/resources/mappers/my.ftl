<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.heyi.${project.dataBase}.platform.${table.tableNameLower}.${table.tableNameUpperCamel}Dao">
    <resultMap id="BaseResultMap" type="com.heyi.${project.dataBase}.platform.${table.tableNameLower}.${table.tableNameUpperCamel}">
        <#list table.columns as column>
            <#if table.key?? && column.columnName==table.key.columnName>
                <id property="${column.columnNameLowerCamel}" column="${column.columnName}" jdbcType="${column.jdbcType}"/>
            <#else>
                <result property="${column.columnNameLowerCamel}" column="${column.columnName}" jdbcType="${column.jdbcType}"/>
            </#if>
        </#list>
    </resultMap>
    <!--获取全部-->
    <select id="getAll" resultMap="BaseResultMap">
        select
        <#list table.columns as column>
            ${column.columnName}<#if column_has_next>,</#if>
        </#list>
        from ${table.tableName}
    </select>
    <!--分页查询-->
    <select id="pages" resultMap="BaseResultMap">
        select
        <#list table.columns as column>
            ${column.columnName}<#if column_has_next>,</#if>
        </#list>
        from ${table.tableName}
        <where>
<#list table.columns as column>
    <#if table.key?? && column.columnName!=table.key.columnName >
        <#if  column.jdbcType != 'BIT' && column.jdbcType != 'TIMESTAMP'&& column.jdbcType != 'INTEGER'&& column.jdbcType != 'DECIMAL'>
            <if test="${column.columnNameLowerCamel} != null and ${column.columnNameLowerCamel} !='' ">
                and ${column.columnName} = #${r'{'}${column.columnNameLowerCamel}${r'}'}
            </if>
        </#if>
        <#if  column.jdbcType == 'BIT'>
            <if test="${column.columnNameLowerCamel} != null ">
                and ${column.columnName} = #${r'{'}${column.columnNameLowerCamel}${r'}'}
            </if>
        </#if>
        <#if  column.jdbcType == 'INTEGER'>
            <if test="${column.columnNameLowerCamel} != null ">
                and ${column.columnName} = #${r'{'}${column.columnNameLowerCamel}${r'}'}
            </if>
        </#if>
        <#if  column.jdbcType == 'TIMESTAMP'>
            <if test="${column.columnNameLowerCamel} != null ">
                and ${column.columnName} = #${r'{'}${column.columnNameLowerCamel}${r'}'}
            </if>
        </#if>
        <#if  column.jdbcType == 'DECIMAL'>
            <if test="${column.columnNameLowerCamel} != null ">
                and ${column.columnName} = #${r'{'}${column.columnNameLowerCamel}${r'}'}
            </if>
        </#if>
    </#if>
</#list>
        </where>
        <if test="orderField != null and orderType != null">
            order by ${r'${orderField} ${orderType}'}
        </if>
        <if test="orderField != null and orderType = null">
            order by ${r'${orderField}'}
        </if>
    </select>
    <!--获取一个根据其他条件-->
    <select id="getone" resultMap="BaseResultMap">
        select
        <#list table.columns as column>
            ${column.columnName}<#if column_has_next>,</#if>
        </#list>
        from ${table.tableName}
        <where>
<#list table.columns as column>
    <#if table.key?? && column.columnName!=table.key.columnName >
        <#if  column.jdbcType != 'BIT' && column.jdbcType != 'TIMESTAMP'&& column.jdbcType != 'INTEGER'&& column.jdbcType != 'DECIMAL'>
            <if test="${column.columnNameLowerCamel} != null and ${column.columnNameLowerCamel} !='' ">
                and ${column.columnName} = #${r'{'}${column.columnNameLowerCamel}${r'}'}
            </if>
        </#if>
        <#if  column.jdbcType == 'BIT'>
            <if test="${column.columnNameLowerCamel} != null ">
                and ${column.columnName} = #${r'{'}${column.columnNameLowerCamel}${r'}'}
            </if>
        </#if>
        <#if  column.jdbcType == 'INTEGER'>
            <if test="${column.columnNameLowerCamel} != null ">
                and ${column.columnName} = #${r'{'}${column.columnNameLowerCamel}${r'}'}
            </if>
        </#if>
        <#if  column.jdbcType == 'TIMESTAMP'>
            <if test="${column.columnNameLowerCamel} != null ">
                and ${column.columnName} = #${r'{'}${column.columnNameLowerCamel}${r'}'}
            </if>
        </#if>
        <#if  column.jdbcType == 'DECIMAL'>
            <if test="${column.columnNameLowerCamel} != null ">
                and ${column.columnName} = #${r'{'}${column.columnNameLowerCamel}${r'}'}
            </if>
        </#if>
    </#if>
</#list>
        </where>
    </select>
    <!--添加-->
    <insert id="add">
        insert into ${table.tableName} (
    <#list table.columns as column>
    ${column.columnName}<#if column_has_next>,</#if>
    </#list> )values (
    <#list table.columns as column>
    #${r'{'}${column.columnNameLowerCamel}${r'}'}<#if column_has_next>,</#if>
    </#list>)
    </insert>
    <!--修改-->
    <update id="update" >
        update ${table.tableName}
        <set>
<#list table.columns as column>
    <#if table.key?? && column.columnName!=table.key.columnName >
        <#if  column.jdbcType != 'BIT' && column.jdbcType != 'TIMESTAMP'&& column.jdbcType != 'INTEGER'&& column.jdbcType != 'DECIMAL'>
            <if test="${column.columnNameLowerCamel} != null and ${column.columnNameLowerCamel} !='' ">
                ${column.columnName} = #${r'{'}${column.columnNameLowerCamel}${r'}'}<#if column_has_next>,</#if>
            </if>
        </#if>
        <#if  column.jdbcType == 'BIT'>
            <if test="${column.columnNameLowerCamel} != null ">
                ${column.columnName} = #${r'{'}${column.columnNameLowerCamel}${r'}'}<#if column_has_next>,</#if>
            </if>
        </#if>
        <#if  column.jdbcType == 'INTEGER'>
            <if test="${column.columnNameLowerCamel} != null ">
                ${column.columnName} = #${r'{'}${column.columnNameLowerCamel}${r'}'}<#if column_has_next>,</#if>
            </if>
        </#if>
        <#if  column.jdbcType == 'TIMESTAMP'>
            <if test="${column.columnNameLowerCamel} != null ">
                ${column.columnName} = #${r'{'}${column.columnNameLowerCamel}${r'}'}<#if column_has_next>,</#if>
            </if>
        </#if>
        <#if  column.jdbcType == 'DECIMAL'>
            <if test="${column.columnNameLowerCamel} != null ">
                ${column.columnName} = #${r'{'}${column.columnNameLowerCamel}${r'}'}<#if column_has_next>,</#if>
            </if>
        </#if>
    </#if>
</#list>
        </set>
        <#if table.key??>
        where ${table.key.columnName}= <#list table.columns as column><#if column.columnName==table.key.columnName>#${r'{'}${column.columnNameLowerCamel}${r'}'} </#if></#list>
        </#if>
    </update>
    <!--详情-->
    <select id="getById" parameterType="java.lang.Long" resultMap="BaseResultMap">
        select
        <#list table.columns as column>
            ${column.columnName}<#if column_has_next>,</#if>
        </#list>
        from ${table.tableName}
        <#if table.key??>
        where ${table.key.columnName}= <#list table.columns as column><#if column.columnName==table.key.columnName>#${r'{'}${column.columnNameLowerCamel}${r'}'} </#if></#list>
        </#if>
    </select>
    <!--删除-->
    <delete id="deleteById" parameterType="java.lang.Long">
        <#if table.key??>
        delete from ${table.tableName} where ${table.key.columnName}= <#list table.columns as column><#if column.columnName==table.key.columnName>#${r'{'}${column.columnNameLowerCamel}${r'}'} </#if></#list>
        </#if>
    </delete>
</mapper>