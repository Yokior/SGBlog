package com.sangeng.domain.vo;

import com.sangeng.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserRole
{
    private List<Long> roleIds;

    private List<Role> roles;

    private UserInfoVo user;

}
