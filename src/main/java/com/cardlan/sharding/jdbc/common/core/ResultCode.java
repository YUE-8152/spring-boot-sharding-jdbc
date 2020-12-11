package com.cardlan.sharding.jdbc.common.core;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResultCode {
    SUCCESS(200,"成功"),
    FAIL(400,"失败"),
    UNAUTHORIZED(401,"未认证（签名错误）"),
    TOKEN_ERROR(401,"无效token"),
    NOT_FOUND(404,"接口不存在"),//接口不存在
    PARAMETERS_ERROR(4100, "参数校验失败"),
    INTERNAL_SERVER_ERROR(500,"系统异常");//服务器内部错误

    private final int code;
    private final String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int code() {
        return code;
    }

    public String msg() {
        return msg;
    }
}
