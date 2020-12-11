package com.cardlan.sharding.jdbc.controller;

import com.cardlan.sharding.jdbc.bean.OrderItemVo;
import com.cardlan.sharding.jdbc.common.core.Result;
import com.cardlan.sharding.jdbc.common.core.ServiceException;
import com.cardlan.sharding.jdbc.common.utils.DateUtils;
import com.cardlan.sharding.jdbc.model.OrderItem;
import com.cardlan.sharding.jdbc.service.OrderItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @Author: YUEXIN
 * @Date: 2020-08-06 15:08:08
 */
@RestController
@RequestMapping("/order/item")
public class OrderItemController {
    @Resource
    private OrderItemService orderItemService;

    @PostMapping("/add")
    public Result add(@RequestBody @Valid OrderItemVo vo) {
        OrderItem orderItem = new OrderItem();
        BeanUtils.copyProperties(vo, orderItem);
        orderItem.setCreateTime(DateUtils.getNowDateTime());
        orderItem.setUpdateTime(DateUtils.getNowDateTime());
        orderItemService.insert(orderItem);
        return Result.success();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        orderItemService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/update")
    public Result update(@RequestBody OrderItem orderItem) {
        orderItemService.update(orderItem);
        return Result.success();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        OrderItem orderItem = orderItemService.findById(id);
        return Result.success(orderItem);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
//        PageHelper.startPage(page, size);
        String startTime = "2020-01-01 00:00:00";
        String endTime = "2020-12-31 23:59:59";
        PageInfo pageInfo = orderItemService.selectOrderItem(startTime, endTime);
        return Result.success(pageInfo);
    }

//    @PostMapping("/select")
//    public String list(@RequestBody OrderItem orderItem) {
//        String name = "";
//        try {
//            List<OrderItem> list = orderItemService.find(orderItem);
//            OrderItem item = list.get(10);
//            name = item.getItemName();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ServiceException("111111111111");
//        }
//        return name;
//    }

    @PostMapping("/select")
    public String list(@RequestBody OrderItem orderItem) {
        String name = "";
        try {
            List<OrderItem> list = orderItemService.find(orderItem);
            OrderItem item = list.get(10);
            name = item.getItemName();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("111111111111");
        }
        return name;
    }

    @PostMapping("/test")
    public Result selectList(@RequestBody OrderItem orderItem) {
        try {
            orderItem.setCreateTime(DateUtils.getNowDateTime());
            Condition condition = new Condition(OrderItem.class);
            Example.Criteria criteria = condition.createCriteria();
            criteria.andBetween("createTime","2020-10-25 00:00:00","2020-12-31 00:00:00");
            List<OrderItem> list = orderItemService.findByCondition(condition);
            return Result.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException();
        }
    }
}
