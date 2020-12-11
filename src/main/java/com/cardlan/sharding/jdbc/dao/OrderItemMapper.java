package com.cardlan.sharding.jdbc.dao;

import com.cardlan.sharding.jdbc.common.core.Mapper;
import com.cardlan.sharding.jdbc.model.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderItemMapper extends Mapper<OrderItem> {
    List<Map<String, Object>> selectOrderItem(@Param("startTime") String startTime, @Param("endTime") String endTime);
}