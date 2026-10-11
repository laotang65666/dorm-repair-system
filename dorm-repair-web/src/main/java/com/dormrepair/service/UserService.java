package com.dormrepair.service;

import com.dormrepair.pojo.LoginResult;
import com.dormrepair.pojo.PageResult;
import com.dormrepair.pojo.User;
import com.dormrepair.pojo.UserQuery;

import java.util.List;

/**
 * 用户业务逻辑接口
 */
public interface UserService {

    /** 登录：校验用户名密码，返回 token + 用户信息 */
    LoginResult login(String username, String password);

    /** 注册（公开，默认学生，可注册维修员） */
    User register(User user);

    /** 按角色查用户列表（派单下拉用，不分页） */
    List<User> listByRole(String role);

    /** 分页查询用户（管理员用，可按角色过滤） */
    PageResult<User> page(UserQuery query);

    /** 管理员创建用户（可指定角色） */
    User addUser(User user);

    /** 管理员修改用户 */
    User updateUser(User user);

    /** 管理员删除用户 */
    void deleteUser(Long id);
}
