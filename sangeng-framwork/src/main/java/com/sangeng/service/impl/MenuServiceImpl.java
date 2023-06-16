package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.entity.Menu;
import com.sangeng.mapper.MenuMapper;
import com.sangeng.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-06-15 14:57:59
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService
{

    // 根据用户ID去查询对应权限信息
    @Override
    public List<String> selectPermsByUserId(Long id)
    {
        // 用户id为1 则是管理员 返回所有权限
        if (id == 1L)
        {
            LambdaQueryWrapper<Menu> lqw = new LambdaQueryWrapper<>();
            lqw.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            lqw.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menuList = list(lqw);

            List<String> perms = menuList.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());

            return perms;
        }

        // 非管理员 查询用户权限信息

        // 查询非管理员权限信息

        return getBaseMapper().selectPermsByUserId(id);
    }
}

