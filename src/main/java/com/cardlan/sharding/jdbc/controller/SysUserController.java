package com.cardlan.sharding.jdbc.controller;
import com.cardlan.sharding.jdbc.common.core.Result;
import com.cardlan.sharding.jdbc.model.SysUser;
import com.cardlan.sharding.jdbc.service.SysUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: YUEXIN
 * @Date: 2020-09-10 16:09:31
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {
    @Resource
    private SysUserService sysUserService;

    @PostMapping("/add")
    public Result add(@RequestBody SysUser sysUser) {
        sysUserService.insert(sysUser);
        return Result.success();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        sysUserService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/update")
    public Result update(@RequestBody SysUser sysUser) {
        sysUserService.update(sysUser);
        return Result.success();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        SysUser sysUser = sysUserService.findById(id);
        return Result.success(sysUser);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<SysUser> list = sysUserService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return Result.success(pageInfo);
    }

    @PostMapping("/select")
    public Result list(@RequestBody SysUser sysUser) {
        List<SysUser> list = sysUserService.find(sysUser);
        return Result.success(list);
    }


}
