package com.heyi.${project.dataBase}.basecommon;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;

/**
 * 基础返回类
 *
 * @author weibo
 * @version 1.0
 * @since 2018/1/7
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
    @ApiModelProperty(value = "返回状态", name = "result", example = "true")
    private Boolean result;
    @ApiModelProperty(value = "描述", name = "description", example = "成功")
    private String description;
    @ApiModelProperty(value = "错误码", name = "code", example = "4001")
    private Integer code;
    @ApiModelProperty(value = "总数", name = "records", example = "20")
    private Integer records;
    @ApiModelProperty(value = "实体对象", name = "obj", example = "{}")
    private Object obj;

    public BaseResponse(Boolean result,String description,Object obj) {
        this.result=result;
        this.description=description;
        this.obj=obj;
    }
    public BaseResponse(Boolean result,String description) {
        this.result=result;
        this.description=description;
    }
    public BaseResponse(Boolean result,String description,Integer code) {
        this.result=result;
        this.description=description;
        this.code=code;
    }
    public BaseResponse(Boolean result,String description,Object obj,Long records) {
        this.result=result;
        this.description=description;
        this.obj=obj;
        this.records=records.intValue();
    }

    public Boolean getResult() {
        return this.result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getObj() {
        return this.obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getRecords() {
        return records;
    }

    public void setRecords(Integer records) {
        this.records = records;
    }
}
