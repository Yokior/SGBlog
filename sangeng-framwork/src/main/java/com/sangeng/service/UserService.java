package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.User;
import com.sangeng.domain.vo.SysUserUpdate;
import com.sangeng.domain.vo.SysUserVo;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-05-31 14:52:44
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult listSysUserInfo(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult addSysUser(SysUserVo sysUserVo);

    ResponseResult deleteSysUser(Long id);

    ResponseResult getSysUserInfo(Long id);

    ResponseResult updateSysUser(SysUserUpdate sysUserUpdate);
}
