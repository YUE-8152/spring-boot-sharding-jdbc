package com.cardlan.sharding.jdbc.common.core;

/**
 * 服务（业务）异常如“ 账号或密码错误 ”，该异常只做INFO级别的日志记录 @see WebMvcConfigurer
 */
public class ServiceException extends RuntimeException {
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public ServiceException setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ServiceException setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public ServiceException() {
        this(500,"服务异常，请联系管理员");
    }

    public ServiceException(String msg) {
        this(500,msg);
    }

    public ServiceException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
