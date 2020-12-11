package com.cardlan.sharding.jdbc.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.cardlan.sharding.jdbc.common.core.Result;
import com.cardlan.sharding.jdbc.common.core.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //验证签名
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            logger.warn("签名认证失败，请求接口：{}，请求IP：{}，请求参数：{}",
                    request.getRequestURI(), getIpAddress(request), JSON.toJSONString(request.getParameterMap()));
            Result result = new Result();
            result.setCode(ResultCode.TOKEN_ERROR.code()).setMsg(ResultCode.TOKEN_ERROR.msg());
            responseResult(response, result);
            return false;
        }
//        String userId = validateToken(token);
        String userId = null;
        if (!StringUtils.isEmpty(userId)) {
            if (handler instanceof HandlerMethod) {
//                LoginUser loginUser = savaLoginInfo(userId);
//                //存储登录相关信息
//                UserContext.setUser(loginUser);
            }
            return true;
        } else {
            logger.warn("签名认证失败，请求接口：{}，请求IP：{}，请求参数：{}",
                    request.getRequestURI(), getIpAddress(request), JSON.toJSONString(request.getParameterMap()));

            Result result = new Result();
            result.setCode(ResultCode.TOKEN_ERROR.code()).setMsg(ResultCode.TOKEN_ERROR.msg());
            responseResult(response, result);
            return false;
        }
    }

    private void responseResult(HttpServletResponse response, Result result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(200);
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }

//    /**
//     * 验证生成的Token是否有效
//     *
//     * @param token
//     * @return
//     */
//    private String validateToken(String token) {
//        String userId = stringRedisTemplate.opsForValue().get(AdRedisKeys.USER_USERID + token);
//        return userId;
//    }
//
//    private LoginUser savaLoginInfo(String userId) {
//        // 3.根据用户Id获取用户信息
//        String userInfo = stringRedisTemplate.opsForValue().get(AdRedisKeys.USER_INFO_BYID + userId);
//        JSONObject user = JSONObject.parseObject(userInfo);
//        LoginUser loginUser = new LoginUser();
//        loginUser.setUserId(userId);
//        loginUser.setMobile(user.getString("mobile"));
//        loginUser.setOpenId(user.getString("openid"));
//        loginUser.setPassword(user.getString("password"));
//        return loginUser;
//    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，那么取第一个ip为客户端ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }

        return ip;
    }
}
