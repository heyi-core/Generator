package  com.heyi.${project.dataBase}.platform.${table.tableNameLower};

import com.heyi.${project.dataBase}.basecommon.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
<#list table.columns as column>
    <#if  column.importType != ''>
${column.importType}
    </#if>
</#list>

/**
* ${table.tableComment}
* @author ${project.author}
* @date  ${project.createTime}
*/
@ApiModel(description = "${table.tableComment}")
@Data
public class ${table.tableNameUpperCamel} extends BaseModel {
<#list table.columns as column>
    @ApiModelProperty(value = "${column.columnComment}", name = "${column.columnNameLowerCamel}", example = "")
    private ${column.javaType} ${column.columnNameLowerCamel};
</#list>
<#if table.tableName == "sys_user" >
    @ApiModelProperty(value = "附属参数_token", name = "token", example = "")
    private String token;
    @ApiModelProperty(value = "附属参数_角色id", name = "sysRoleId", example = "")
    private Long sysRoleId;
    @ApiModelProperty(value = "附属参数_角色名称", name = "sysRoleName", example = "")
    private String sysRoleName;
    @ApiModelProperty(value = "附属参数_ip", name = "ip", example = "")
    private String ip;
</#if>
}