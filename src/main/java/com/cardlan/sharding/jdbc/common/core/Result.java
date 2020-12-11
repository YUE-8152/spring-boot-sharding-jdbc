package com.cardlan.sharding.jdbc.common.core;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;

/**
 * 统一API响应结果封装
 */
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public Result setCode(int code) {
        this.code = code;
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Result setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result setData(T data) {
        this.data = data;
        return this;
    }

    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    public static Result success() {
        return new Result()
                .setCode(ResultCode.SUCCESS.code())
                .setMsg(DEFAULT_SUCCESS_MESSAGE);
    }

    public static Result success(PageInfo pageInfo) {
        return new Result()
                .setCode(ResultCode.SUCCESS.code())
                .setMsg(DEFAULT_SUCCESS_MESSAGE)
                .setData(new Result.BaseResult(pageInfo.getList(), pageInfo.getTotal()));
    }

    public static <T> Result<T> success(T data) {
        return new Result()
                .setCode(ResultCode.SUCCESS.code())
                .setMsg(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static Result fail(String message) {
        return new Result()
                .setCode(ResultCode.FAIL.code())
                .setMsg(message);
    }

    public static Result fail(int code, String message) {
        return new Result()
                .setCode(code)
                .setMsg(message);
    }

    public static Result fail(int code, String message, Object date) {
        return new Result()
                .setCode(code)
                .setMsg(message).setData(date);
    }

    public static Result systemError() {
        return new Result()
                .setCode(ResultCode.INTERNAL_SERVER_ERROR.code())
                .setMsg(ResultCode.INTERNAL_SERVER_ERROR.msg());
    }

    private static class BaseResult {
        private Object list;
        private Long total;

        public Object getList() {
            return list;
        }

        public void setList(Object list) {
            this.list = list;
        }

        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }

        public BaseResult(Object list, Long total) {
            this.list = list;
            this.total = total;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
