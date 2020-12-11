package com.cardlan.sharding.jdbc.common.utils;

import java.util.UUID;

public class TraceUtils {
    /**
     * 线程组
     */
    private static final ThreadLocal<String> TRACE_ID = new ThreadLocal<String>();

    /**
     * 获取当前线程的traceId
     * @return
     */
    public static String getTraceId()
    {
        if (TRACE_ID.get() == null)
        {
            String s = UUID.randomUUID().toString();
            setTraceId(s);
        }
        return TRACE_ID.get();
    }


    /**
     * 获取当前线程的traceId
     * @return
     */
    public static String getNewTraceId()
    {
        String s = UUID.randomUUID().toString();
        setTraceId(s);
        return TRACE_ID.get();
    }

    /**
     * 将traceId设置到当前线程
     * @param traceId
     */
    public static void setTraceId(String traceId)
    {
        TRACE_ID.set(traceId);
    }
}
