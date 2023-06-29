package com.sangeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sangeng.domain.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色和菜单关联表(RoleMenu)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-29 21:54:37
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

}

