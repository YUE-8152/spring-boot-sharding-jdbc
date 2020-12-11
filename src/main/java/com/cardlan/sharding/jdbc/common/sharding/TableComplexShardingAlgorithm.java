package com.cardlan.sharding.jdbc.common.sharding;

import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * @ProjectName: spring-boot-sharding-jdbc
 * @Package: com.cardlan.sharding.jdbc.common.config
 * @ClassName: TableComplexShardingAlgorithm
 * @Author: YUE
 * @Description:
 * @Date: 2020/12/1 11:11
 * @Version: 1.0
 */
@Component
public class TableComplexShardingAlgorithm implements ComplexKeysShardingAlgorithm<Comparable<?>> {
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ComplexKeysShardingValue<Comparable<?>> shardingValue) {
        return ShardingTableUtils.doSharding(availableTargetNames, shardingValue, "create_time");
    }
}
