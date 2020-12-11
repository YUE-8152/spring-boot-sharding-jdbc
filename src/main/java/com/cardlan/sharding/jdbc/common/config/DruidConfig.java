package com.cardlan.sharding.jdbc.common.config;

import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: spring-boot-basic-framework
 * @Package: com.cardlan.mall.common.config
 * @ClassName: DruidConfig
 * @Author: YUE
 * @Description:
 * @Date: 2020/9/29 15:18
 * @Version: 1.0
 */
@Configuration
public class DruidConfig {
    /**
     * Druid监控页面相关配置,可通过 http://localhost:8081/druid/index.html 访问
     *
     * @return: org.springframework.boot.web.servlet.ServletRegistrationBean
     * @Author: YUE
     * @Date: 2020/9/29 14:08
     **/
    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");
        Map<String, String> initParameters = new HashMap<>();
        // 禁用HTML页面上的“Rest All”功能
        initParameters.put("resetEnable", "false");
        // ip白名单（没有配置或者为空，则允许所有访问）
        initParameters.put("allow", "");
        // 监控页面登录用户名
        initParameters.put("loginUsername", "admin");
        // 监控页面登录用户密码
        initParameters.put("loginPassword", "123456");
        // ip黑名单
        initParameters.put("deny", "");
        // 如果某个ip同时存在，deny优先于allow
        servletRegistrationBean.setInitParameters(initParameters);
        return servletRegistrationBean;
    }
}
