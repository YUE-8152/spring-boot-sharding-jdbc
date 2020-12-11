package com.cardlan.sharding.jdbc.service;
import com.cardlan.sharding.jdbc.model.OrderItem;
import com.cardlan.sharding.jdbc.common.core.Service;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * @Author: YUEXIN
 * @Date: 2020-08-06 15:08:08
 */
public interface OrderItemService extends Service<OrderItem> {

    void testTransactional();

    PageInfo selectOrderItem(String startTime, String endTime);
}
