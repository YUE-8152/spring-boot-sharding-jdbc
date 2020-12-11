package com.cardlan.sharding.jdbc.service.impl;

import com.cardlan.sharding.jdbc.dao.SysUserMapper;
import com.cardlan.sharding.jdbc.model.SysUser;
import com.cardlan.sharding.jdbc.service.SysUserService;
import com.cardlan.sharding.jdbc.common.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * @Author: YUEXIN
 * @Date: 2020-09-10 16:09:31
 */
@Service
@Transactional
public class SysUserServiceImpl extends AbstractService<SysUser> implements SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;

}
