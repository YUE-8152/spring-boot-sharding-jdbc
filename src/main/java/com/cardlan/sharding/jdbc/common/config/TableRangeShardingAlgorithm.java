package com.cardlan.sharding.jdbc.common.config;

import com.cardlan.sharding.jdbc.common.utils.DateUtils;
import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @ProjectName: spring-boot-sharding-jdbc
 * @Package: com.cardlan.sharding.jdbc.common.config
 * @ClassName: TableRangeShardingAlgorithm
 * @Author: YUE
 * @Description:
 * @Date: 2020/12/1 10:16
 * @Version: 1.0
 */
@Component
public class TableRangeShardingAlgorithm implements RangeShardingAlgorithm<Date> {
    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Date> rangeShardingValue) {
        Collection<String> collect = new ArrayList<>();
        Range<Date> valueRange = rangeShardingValue.getValueRange();
        String between = rangeShardingValue.getLogicTableName() + "_" + DateUtils.getDateToStringYM(valueRange.lowerEndpoint());
        String and = rangeShardingValue.getLogicTableName() + "_" + DateUtils.getDateToStringYM(valueRange.upperEndpoint());
        for (String each : collection) {
            if (between.equals(each) || and.equals(each)) {
                collect.add(each);
            }
        }
        return collect;
    }
}
