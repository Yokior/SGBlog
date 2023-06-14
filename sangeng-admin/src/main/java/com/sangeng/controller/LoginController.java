package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.User;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.service.BlogLoginService;
import com.sangeng.service.LoginService;
import io.jsonwebtoken.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController
{
    @Autowired
    private LoginService loginService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user)
    {
        if (!Strings.hasText(user.getUserName()))
        {
            // 提示 需要传入用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }

        return loginService.login(user);
    }


    @PostMapping("/user/logout")
    public ResponseResult logout()
    {
        return loginService.logout();
    }
}
