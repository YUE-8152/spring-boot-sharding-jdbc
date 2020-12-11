package com.cardlan.sharding.jdbc.common.config;

import com.cardlan.sharding.jdbc.common.core.ServiceException;
import com.cardlan.sharding.jdbc.common.utils.DateUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

/**
 * @ProjectName: spring-boot-sharding-jdbc
 * @Package: com.cardlan.sharding.jdbc.common.config
 * @ClassName: ShardingAlgorithm
 * @Author: YUE
 * @Description:
 * @Date: 2020/11/30 14:22
 * @Version: 1.0
 */
@Component
public class ShardingTableAlgorithm implements PreciseShardingAlgorithm<Date> {
    private static final Logger logger = LoggerFactory.getLogger(ShardingTableAlgorithm.class);

    @Override
    public String doSharding(Collection<String> tableNames, PreciseShardingValue<Date> preciseShardingValue) {
        String ym = DateUtils.getDateToStringYM(preciseShardingValue.getValue());
        String tableName = preciseShardingValue.getLogicTableName() + "_" + ym;
        logger.info("=======>当前所有表名：{}", tableNames);
        System.out.println("=======>当前所有表名：" + tableNames);
        if (tableNames.contains(tableName)) {
            logger.info("=======>当前表名：{}", tableName);
            System.out.println("=======>当前表名：" + tableName);
            return tableName;
        }
        throw new ServiceException("无效表名：" + tableName);
    }

}
