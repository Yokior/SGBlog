package com.sangeng.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserUpdate
{
    private Long id;

    private String userName;

    private String nickName;

    private String email;

    private String sex;

    private String status;

    private List<Long> roleIds;
}
