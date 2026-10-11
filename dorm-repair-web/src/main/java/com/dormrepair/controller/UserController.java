package com.dormrepair.controller;

import com.dormrepair.anno.Log;
import com.dormrepair.anno.RequireRole;
import com.dormrepair.pojo.Result;
import com.dormrepair.pojo.User;
import com.dormrepair.pojo.UserQuery;
import com.dormrepair.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理接口。
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /** 注册（公开）：POST /user/register */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        return Result.success(userService.register(user));
    }

    /** 分页查询用户（仅管理员）：GET /user/page?page=1&pageSize=10&role= */
    @RequireRole("admin")
    @GetMapping("/page")
    public Result page(UserQuery query) {
        return Result.success(userService.page(query));
    }

    /** 按角色查用户（仅管理员，派单下拉用）：GET /user/list?role=worker */
    @RequireRole("admin")
    @GetMapping("/list")
    public Result list(@RequestParam String role) {
        return Result.success(userService.listByRole(role));
    }

    /** 管理员创建用户：POST /user/add */
    @Log
    @RequireRole("admin")
    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        return Result.success(userService.addUser(user));
    }

    /** 管理员修改用户：PUT /user */
    @Log
    @RequireRole("admin")
    @PutMapping
    public Result update(@RequestBody User user) {
        return Result.success(userService.updateUser(user));
    }

    /** 管理员删除用户：DELETE /user/{id} */
    @Log
    @RequireRole("admin")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }
}
