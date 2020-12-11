package com.cardlan.sharding.jdbc.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


@Configuration
public class RedissonConfig {

    @Value("${redisson.address}")
    private String urls;
    @Value("${redisson.password}")
    private String password;

    /**
     * 单机版
     *
     * @return
     * @throws IOException
     */
    @Bean(name = "redissonClient")
    public RedissonClient redissonClientSingle() throws IOException {
        RedissonClient redisson = null;
        Config config = new Config();
        config.useSingleServer().setAddress(urls)
                .setPassword(password)
                .setConnectionPoolSize(10)
                .setConnectionMinimumIdleSize(2)
                .setIdleConnectionTimeout(5000)
                .setTimeout(5000);
        redisson = Redisson.create(config);
        // 可通过打印redisson.getConfig().toJSON().toString()来检测是否配置成功
        return redisson;
    }

    /**
     * 集群
     *
     * @return
     * @throws IOException
     */
//    @Bean(name = "redissonClient")
//    public RedissonClient redissonClientCluster() throws IOException {
//        String[] nodes = urls.split(",");
//        // redisson版本是3.5，集群的ip前面要加上“redis://”，不然会报错，3.2版本可不加
//        for (int i = 0; i < nodes.length; i++) {
//            nodes[i] = "redis://" + nodes[i];
//        }
//        RedissonClient redisson = null;
//        Config config = new Config();
//        config.useClusterServers() // 这是用的集群server
//                .setScanInterval(2000) // 设置集群状态扫描时间
//                .addNodeAddress(nodes).setPassword(password);
//        redisson = Redisson.create(config);
//        // 可通过打印redisson.getConfig().toJSON().toString()来检测是否配置成功
//        return redisson;
//    }
}
