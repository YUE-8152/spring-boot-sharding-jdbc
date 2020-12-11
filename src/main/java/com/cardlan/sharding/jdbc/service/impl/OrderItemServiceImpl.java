package com.cardlan.sharding.jdbc.service.impl;

import com.cardlan.sharding.jdbc.dao.OrderItemMapper;
import com.cardlan.sharding.jdbc.dao.SysUserMapper;
import com.cardlan.sharding.jdbc.model.OrderItem;
import com.cardlan.sharding.jdbc.service.OrderItemService;
import com.cardlan.sharding.jdbc.common.core.AbstractService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * @Author: YUEXIN
 * @Date: 2020-08-06 15:08:08
 */
@Service
public class OrderItemServiceImpl extends AbstractService<OrderItem> implements OrderItemService {
    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private SysUserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void testTransactional() {
        deleteSysUser(3);
//        OrderItem orderItem = new OrderItem();
//        orderItem.setItemName("HH");
//        orderItem.setItemCode("11111111111111111111111111111111111111111111111111111111111111111111111");
//        orderItemMapper.insert(orderItem);
    }

    @Override
    public PageInfo selectOrderItem(String startTime, String endTime) {
        PageHelper.startPage(1, 5);
        List<Map<String, Object>> list = orderItemMapper.selectOrderItem(startTime, endTime);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    private void deleteSysUser(Integer id) {
        userMapper.deleteByPrimaryKey(id);
    }
}
