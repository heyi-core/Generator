package com.heyi.${project.dataBase}.basecommon;

public class BaseException extends RuntimeException {
    private Integer code = 999999;
    private String msg;

    public BaseException() {
    }

    public BaseException(int code) {
        this.code = code;
    }

    public BaseException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BaseException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
