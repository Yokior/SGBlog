package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Role;
import com.sangeng.domain.vo.RoleInfoVo;
import com.sangeng.domain.vo.RoleVo;

import java.util.List;
import java.util.Map;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-06-15 15:01:10
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult listRoles(int pageNum, int pageSize, String roleName, String status);

    ResponseResult changeStatus(Map<String,String> roleMap);

    ResponseResult addRole(RoleVo roleVo);

    ResponseResult getRoleInfo(Long id);

    ResponseResult updateRoleInfo(RoleVo roleVo);

    ResponseResult deleteRole(Long id);

    ResponseResult listAllRole();
}
