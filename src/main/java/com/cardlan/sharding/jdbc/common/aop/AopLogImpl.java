package com.cardlan.sharding.jdbc.common.aop;

import com.alibaba.fastjson.JSONObject;
import com.cardlan.sharding.jdbc.common.utils.TraceUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author YUEXIN
 * @ClassName: AopLogImpl
 * @Description: 切面日志输出注解实现类
 */
@Aspect
@Component
public class AopLogImpl {
    private Logger logger = LoggerFactory.getLogger(AopLogImpl.class);

    @Pointcut("@annotation(com.cardlan.sharding.jdbc.common.aop.AopLog)")
    public void annoLogAspect() {

    }

    @Around("annoLogAspect()")
    public Object annoBefore(ProceedingJoinPoint joinPoint) {
        MethodSignature sign = (MethodSignature) joinPoint.getSignature();
        Method method = sign.getMethod();
        AopLog annotation = method.getAnnotation(AopLog.class);
        try {
            // 方法施行前
            logger.info(TraceUtils.getTraceId() + "- " + String.format("接收数据：url=%s&args=%s", annotation.value(), Arrays.toString(joinPoint.getArgs())));

            // 方法施行后
            Object ret = joinPoint.proceed();
            if (ret != null) {
                if (ret.getClass().isPrimitive()) {
                    logger.info(TraceUtils.getTraceId() + "- " + String.format("返回数据：%s", ret));
                } else {
                    logger.info(TraceUtils.getTraceId() + "- " + String.format("返回数据：%s", JSONObject.toJSON(ret).toString()));
                }
            }
            return ret;
        } catch (Throwable t) {
            t.printStackTrace();
            logger.error(TraceUtils.getTraceId() + "- " + String.format("================错误信息================== \n %s \n %s", annotation.value(), t.getMessage()));
            return null;
        }
    }

    //    @Around("execution(* com.cardlan.mall.controller.*.*(..))")
    public Object reqBefore(ProceedingJoinPoint joinPoint) {
        TraceUtils.getNewTraceId();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        logger.info(TraceUtils.getTraceId() + "- " + String.format("http请求: url=%s&args=%s", request.getRequestURI(), Arrays.toString(joinPoint.getArgs())));

        try {
            // 方法施行后
            Object ret = joinPoint.proceed();
            if (ret != null) {
                if (ret.getClass().isPrimitive()) {
                    logger.info(TraceUtils.getTraceId() + "- " + String.format("http返回: %s", ret));
                } else {
                    logger.info(TraceUtils.getTraceId() + "- " + String.format("http返回: %s", JSONObject.toJSON(ret).toString()));
                }
            }
            return ret;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
}
