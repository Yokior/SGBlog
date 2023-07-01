package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.User;
import com.sangeng.domain.entity.UserRole;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.domain.vo.SysUserInfoVo;
import com.sangeng.domain.vo.SysUserVo;
import com.sangeng.domain.vo.UserInfoVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.mapper.UserMapper;
import com.sangeng.service.RoleService;
import com.sangeng.service.UserRoleService;
import com.sangeng.service.UserService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-05-31 14:52:44
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService
{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public ResponseResult userInfo()
    {
        // 获取用户ID
        Long userId = SecurityUtils.getUserId();

        // 根据用户ID查询信息
        User user = getById(userId);

        // 封装成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user)
    {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user)
    {
        // 对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName()))
        {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword()))
        {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail()))
        {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName()))
        {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }

        // 对数据进行是否存在判断
        if (userNameExist(user.getUserName()))
        {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(user.getNickName()))
        {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }

        // 对密码进行加密 存储
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);

        // 存入数据库
        save(user);
        return ResponseResult.okResult(user);
    }

    @Override
    public ResponseResult listSysUserInfo(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status)
    {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        // 用户名模糊搜索
        lqw.like(StringUtils.hasText(userName),User::getUserName,userName);
        // 手机号搜索
        lqw.eq(StringUtils.hasText(phonenumber),User::getPhonenumber,phonenumber);
        // 状态搜索
        lqw.eq(StringUtils.hasText(status),User::getStatus,status);

        // 分页
        Page<User> userPage = new Page<>(pageNum, pageSize);
        page(userPage,lqw);

        // 转换为vo返回
        List<SysUserInfoVo> sysUserInfoVoList = BeanCopyUtils.copyBeanList(userPage.getRecords(), SysUserInfoVo.class);

        PageVo pageVo = new PageVo(sysUserInfoVoList, userPage.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    @Transactional
    public ResponseResult addSysUser(SysUserVo sysUserVo)
    {
        // 对数据进行非空判断
        if (!StringUtils.hasText(sysUserVo.getUserName()))
        {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(sysUserVo.getPassword()))
        {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(sysUserVo.getEmail()))
        {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(sysUserVo.getNickName()))
        {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }

        // 对数据进行是否存在判断
        if (userNameExist(sysUserVo.getUserName()))
        {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(sysUserVo.getNickName()))
        {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (phonenumberExist(sysUserVo.getPhonenumber()))
        {
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        if (emailExist(sysUserVo.getEmail()))
        {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        // 对密码进行加密 存储
        String encode = passwordEncoder.encode(sysUserVo.getPassword());
        sysUserVo.setPassword(encode);

        // 存储user
        User user = BeanCopyUtils.copyBean(sysUserVo, User.class);
        save(user);

        // 存储role信息
        Long userId = user.getId();
        List<UserRole> userRoleList = sysUserVo.getRoleIds().stream()
                .map(roleId -> new UserRole(userId, roleId))
                .collect(Collectors.toList());

        // 存储user-role关联信息
        userRoleService.saveBatch(userRoleList);

        return ResponseResult.okResult();
    }

    private boolean nickNameExist(String nickName)
    {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getNickName,nickName);
        int count = count(lqw);
        return count > 0;
    }

    private boolean userNameExist(String userName)
    {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getUserName,userName);
        int count = count(lqw);
        return count > 0;
    }

    private boolean phonenumberExist(String phonenumber)
    {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getPhonenumber,phonenumber);
        int count = count(lqw);
        return count > 0;
    }

    private boolean emailExist(String email)
    {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getEmail,email);
        int count = count(lqw);
        return count > 0;
    }
}

