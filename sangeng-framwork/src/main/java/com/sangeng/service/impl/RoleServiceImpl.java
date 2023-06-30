package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Role;
import com.sangeng.domain.entity.RoleMenu;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.domain.vo.RoleInfoVo;
import com.sangeng.domain.vo.RoleVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.mapper.RoleMapper;
import com.sangeng.service.RoleMenuService;
import com.sangeng.service.RoleService;
import com.sangeng.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-06-15 15:01:10
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService
{

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id)
    {
        // 判断是否为管理员 如果是集合只需要返回admin
        if (id == 1L)
        {
            ArrayList<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }

        // 否则查询用户具有的角色信息

        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult listRoles(int pageNum, int pageSize, String roleName, String status)
    {
        // 根据roleName status查询role
        LambdaQueryWrapper<Role> lqw = new LambdaQueryWrapper<>();
        lqw.like(roleName != null, Role::getRoleName, roleName);
        lqw.eq(status != null, Role::getStatus, status);

        // 根据roleSort进行升序排列
        lqw.orderByAsc(Role::getRoleSort);

        // 分页
        Page<Role> rolePage = new Page<>(pageNum, pageSize);
        page(rolePage,lqw);

        if (rolePage.getTotal() <= 0)
        {
            return ResponseResult.errorResult(AppHttpCodeEnum.ROLE_NOT_EXIST);
        }

        // 转换为vo
        List<RoleVo> roleVoList = BeanCopyUtils.copyBeanList(rolePage.getRecords(), RoleVo.class);

        // 转换为PageVo
        PageVo pageVo = new PageVo(roleVoList, rolePage.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changeStatus(Map<String,String> roleMap)
    {
        // 根据角色Id获取角色
        Role role = getById(roleMap.get("roleId"));
        if (role == null)
        {
            return ResponseResult.errorResult(AppHttpCodeEnum.ROLE_NOT_EXIST);
        }

        // 修改角色信息
        role.setStatus(roleMap.get("status"));

        updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addRole(RoleVo roleVo)
    {
        // 创建role对象 拷贝添加
        Role role = BeanCopyUtils.copyBean(roleVo, Role.class);
        save(role);

        // role-menu关联表更新
        Long roleId = role.getId();
        List<RoleMenu> roleMenuList = roleVo.getMenuIds().stream()
                .map(menuId -> new RoleMenu(roleId, menuId))
                .collect(Collectors.toList());

        roleMenuService.saveBatch(roleMenuList);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRoleInfo(Long id)
    {
        // 根据id获取role信息
        Role role = getById(id);
        if (role == null)
        {
            return ResponseResult.errorResult(AppHttpCodeEnum.ROLE_NOT_EXIST);
        }

        // 封装RoleInfoVo返回
        RoleInfoVo roleInfoVo = BeanCopyUtils.copyBean(role, RoleInfoVo.class);

        return ResponseResult.okResult(roleInfoVo);
    }

    @Override
    public ResponseResult updateRoleInfo(RoleVo roleVo)
    {
        // 获取role对象 更新role表
        Long roleId = roleVo.getId();

        Role role = getById(roleId);
        if (role == null)
        {
            return ResponseResult.errorResult(AppHttpCodeEnum.ROLE_NOT_EXIST);
        }

        role.setRoleKey(roleVo.getRoleKey());
        role.setRoleName(roleVo.getRoleName());
        role.setRoleSort(roleVo.getRoleSort());
        role.setStatus(roleVo.getStatus());
        role.setRemark(roleVo.getRemark());

        updateById(role);

        // 更新role-menu关联表数据
        List<RoleMenu> roleMenuList = roleVo.getMenuIds().stream()
                .map(menuId -> new RoleMenu(roleId, menuId))
                .collect(Collectors.toList());

        // 注意 该表有两个主键 所以先删除再插入
        LambdaQueryWrapper<RoleMenu> lqw = new LambdaQueryWrapper<>();
        lqw.eq(RoleMenu::getRoleId,roleId);
        roleMenuService.remove(lqw);

        roleMenuService.saveBatch(roleMenuList);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRole(Long id)
    {
        // 查询角色是否存在
        Role role = getById(id);
        if (role == null)
        {
            return ResponseResult.errorResult(AppHttpCodeEnum.ROLE_NOT_EXIST);
        }

        // 逻辑删除角色
        removeById(id);

        return ResponseResult.okResult();
    }

}

