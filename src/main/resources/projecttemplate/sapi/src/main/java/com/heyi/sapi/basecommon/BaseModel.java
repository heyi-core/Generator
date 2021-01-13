package com.heyi.${project.dataBase}.basecommon;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;

/*@Transient表示该属性并非一个到数据库表的字段的映射*/
/*@JsonInclude表示字段为null就不会显示*/
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BaseModel  implements  Serializable{

    @Transient
    @ApiModelProperty(value = "起始页", example = "0")
    private Integer start;
    @Transient
    @ApiModelProperty(value = "展示数量", example = "10")
    private Integer length;
    @Transient
    @ApiModelProperty(value = "排序字段(create_time,   trademark_price)", example = "10")
    private String orderField;
    @Transient
    @ApiModelProperty(value = "排序类型(asc, desc)-", example = "10")
    private String orderType;
    @Transient
    @ApiModelProperty(value = "通知内容", name = "messageContent", example = "")
    private String messageContent;
}