package com.heyi.${project.dataBase}.platform.${table.tableNameLower}4es;

import com.heyi.${project.dataBase}.basecommon.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
<#list table.columns as column>
<#if  column.importType != ''>
${column.importType}
</#if>
</#list>

/**
 * ${table.tableComment}
 * @author ${project.author}
 * @date  ${project.createTime}
 * 特殊匹配字段 自己修改filed 例如简介 内容 名称
 *  @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
 */
@ApiModel(description = "${table.tableComment}es搜索接口")
@Data
@Document(indexName = "${table.tableNameLower}4es",type = "_doc")
public class ${table.tableNameUpperCamel}4es extends BaseModel {
    <#list table.columns as column>
        <#if column.columnName == table.key.columnName>
    @Id
    @ApiModelProperty(value = "${column.columnComment}", name = "${column.columnNameLowerCamel}", example = "")
    @Field(type = FieldType.Long)
    private ${column.javaType} ${column.columnNameLowerCamel};
        </#if>
        <#if column.columnName != table.key.columnName>
    @ApiModelProperty(value = "${column.columnComment}", name = "${column.columnNameLowerCamel}", example = "")
    @Field(type = ${column.esType})
    private ${column.javaType} ${column.columnNameLowerCamel};
        </#if>
    </#list>

    @ApiModelProperty(value = "地理位置存储到es", name = "location", example = "")
    @GeoPointField
    private GeoPoint location;
    @ApiModelProperty(value = "es查询范围", name = "range", example = "")
    private String  range;
    @ApiModelProperty(value = "全文索引查询", name = "searchWord", example = "")
    private  String searchWord;
}