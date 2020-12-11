package com.cardlan.sharding.jdbc.common.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @ProjectName: spring-boot-basic-framework
 * @Package: com.cardlan.mall.common.core
 * @ClassName: ResponseAdvice
 * @Author: YUE
 * @Description: 对Controller层返回类型进行统一包装
 * @Date: 2020/8/6 16:01
 * @Version: 1.0
 */
@RestControllerAdvice(basePackages = {"com.cardlan.mall.controller"}) // 注意哦，这里要加上需要扫描的包
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // 如果接口返回的类型本身就是Result那就没有必要进行额外的操作，返回false
        return !methodParameter.getGenericParameterType().equals(Result.class);
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // String类型不能直接包装，所以要进行些特别的处理
        if (methodParameter.getGenericParameterType().equals(String.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 将数据包装在Result里后，再转换为json字符串响应给前端
                return objectMapper.writeValueAsString(Result.success(o));
            } catch (JsonProcessingException e) {
                throw new ServiceException("返回String类型错误");
            }
        }
        // 将原本的数据包装在Result里
        return Result.success(o);
    }
}
