package com.dormrepair.controller;

import com.dormrepair.pojo.LoginResult;
import com.dormrepair.pojo.Result;
import com.dormrepair.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 登录接口（无需 token，已在拦截器中放行）。
 */
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    /** POST /login，请求体 {username, password}，返回 {token, user} */
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> body) {
        log.info("登录请求：{}", body.get("username"));
        LoginResult result = userService.login(body.get("username"), body.get("password"));
        return Result.success(result);
    }
}
