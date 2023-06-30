package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Menu;
import com.sangeng.domain.entity.Role;
import com.sangeng.domain.entity.RoleMenu;
import com.sangeng.domain.vo.MenuVo;
import com.sangeng.domain.vo.RoleMenuVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.mapper.MenuMapper;
import com.sangeng.service.MenuService;
import com.sangeng.service.RoleMenuService;
import com.sangeng.service.RoleService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMenuService roleMenuService;

    // 根据用户ID去查询对应权限信息
    @Override
    public List<String> selectPermsByUserId(Long id)
    {
        // 用户id为1 则是管理员 返回所有权限
        if (id == 1L)
        {
            LambdaQueryWrapper<Menu> lqw = new LambdaQueryWrapper<>();
            lqw.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            lqw.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
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

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId)
    {
        List<Menu> menus = null;
        // 判断是否是管理员
        if (userId == 1L)
        {
            // 如果是 返回所有符合要求的menu
            menus = getBaseMapper().selectAllRouterMenu();
        }
        else
        {
            // 否 返回对应的menu
            menus = getBaseMapper().selectRouterMenuTreeByUserId(userId);
        }

        // 构建tree

        List<Menu> menuTree = builderMenuTree(menus,0L);

        return menuTree;
    }

    /**
     * 获取全部的菜单树
     * @return
     */
    @Override
    public List<MenuVo> treeSelect()
    {
        // 获取菜单树接口
        // 获取所有菜单
        List<Menu> menuList = list();
        List<MenuVo> menuVoList = BeanCopyUtils.copyBeanList(menuList, MenuVo.class);

        menuVoList.stream()
                .forEach(menuVo ->
                {
                    Menu menu = getById(menuVo.getId());
                    menuVo.setLabel(menu.getMenuName());
                });

        // 构建树结构
        List<MenuVo> menuTreeVo = builderMenuTreeVo(menuVoList, 0L);

        return menuTreeVo;
    }

    /**
     * 根据权限信息获取指定的菜单树
     * @param menuIds 权限信息
     * @return
     */
    public List<MenuVo> treeSelectByPerm(List<Long> menuIds)
    {
        // 空值
        if (menuIds == null)
        {
            return null;
        }

        // 获取指定的菜单树
        LambdaQueryWrapper<Menu> lqw = new LambdaQueryWrapper<>();
        lqw.in(Menu::getId,menuIds);

        List<Menu> menuList = list(lqw);
        List<MenuVo> menuVoList = BeanCopyUtils.copyBeanList(menuList, MenuVo.class);

        menuVoList.stream()
                .forEach(menuVo ->
                {
                    Menu menu = getById(menuVo.getId());
                    menuVo.setLabel(menu.getMenuName());
                });

        // 构建树结构
        List<MenuVo> menuTreeVo = builderMenuTreeVo(menuVoList, 0L);

        return menuTreeVo;
    }

    /**
     * 加载对应角色菜单列表树
     * @param id
     * @return
     */
    @Override
    public ResponseResult roleMenuTreeselect(Long id)
    {
        RoleMenuVo roleMenuVo = new RoleMenuVo();

        // 判断是不是超级管理员
        if (id == 1L)
        {
            // 查询全部权限信息
            List<MenuVo> menuVoList = treeSelect();
            List<Long> menuIds = list().stream()
                    .map(Menu::getId)
                    .collect(Collectors.toList());

            roleMenuVo.setCheckedKeys(menuIds);
            roleMenuVo.setMenus(menuVoList);

            return ResponseResult.okResult(roleMenuVo);
        }

        // 根据id获取role
        Role role = roleService.getById(id);
        if (role == null)
        {
            return ResponseResult.errorResult(AppHttpCodeEnum.ROLE_NOT_EXIST);
        }

        // 根据role-menu关联表获取权限信息
        LambdaQueryWrapper<RoleMenu> lqw = new LambdaQueryWrapper<>();
        lqw.eq(RoleMenu::getRoleId,role.getId());
        lqw.select(RoleMenu::getMenuId);
        List<RoleMenu> roleMenuList = roleMenuService.list(lqw);
        // 转换为menuId集合
        List<Long> menuIds = roleMenuList.stream()
                .map(RoleMenu::getMenuId)
                .collect(Collectors.toList());
        // 权限信息存入vo
        roleMenuVo.setCheckedKeys(menuIds);

        // 根据权限信息获取指定的菜单树
        List<MenuVo> menuVoList = treeSelectByPerm(menuIds);

        // 封装数据返回
        roleMenuVo.setMenus(menuVoList);

        return ResponseResult.okResult(roleMenuVo);
    }

    private List<Menu> builderMenuTree(List<Menu> menus, long parentId)
    {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());

        return menuTree;
    }

    private List<MenuVo> builderMenuTreeVo(List<MenuVo> menus, long parentId)
    {
        List<MenuVo> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());

        return menuTree;
    }


    /**
     * 获取存入参数的子menu
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus)
    {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .collect(Collectors.toList());

        return childrenList;
    }

    private List<MenuVo> getChildren(MenuVo menu, List<MenuVo> menus)
    {
        List<MenuVo> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                // 多层子菜单
                .map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());

        return childrenList;
    }
}

