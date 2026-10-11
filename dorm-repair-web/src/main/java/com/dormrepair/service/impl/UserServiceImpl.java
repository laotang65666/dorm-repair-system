package com.dormrepair.service.impl;

import com.dormrepair.common.TokenService;
import com.dormrepair.exception.BusinessException;
import com.dormrepair.mapper.UserMapper;
import com.dormrepair.pojo.LoginResult;
import com.dormrepair.pojo.PageResult;
import com.dormrepair.pojo.User;
import com.dormrepair.pojo.UserQuery;
import com.dormrepair.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TokenService tokenService;

    /** BCrypt 密码校验器：数据库存加密后的密码，用它比对「明文」和「加密串」 */
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public LoginResult login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new BusinessException("用户名或密码不能为空");
        }
        User user = userMapper.selectByUsername(username);
        // 用户不存在或密码不匹配，统一提示「用户名或密码错误」（不暴露用户名是否存在）
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用，请联系管理员");
        }
        String token = tokenService.login(user);
        user.setPassword(null); // 不把密码返回给前端
        return new LoginResult(token, user);
    }

    @Override
    public User register(User user) {
        validateUserInput(user);
        if (userMapper.selectByUsername(user.getUsername()) != null) {
            throw new BusinessException("用户名已存在");
        }
        // 密码加密存储
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 自助注册只允许学生/维修员，非法角色（如 admin）一律降级为学生
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("student");
        } else if (!"student".equals(user.getRole()) && !"worker".equals(user.getRole())) {
            user.setRole("student");
        }
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);
        user.setPassword(null);
        return user;
    }

    @Override
    public List<User> listByRole(String role) {
        return userMapper.listByRole(role);
    }

    @Override
    public PageResult<User> page(UserQuery query) {
        Long total = userMapper.count(query);
        List<User> rows = userMapper.list(query);
        return new PageResult<>(total, rows);
    }

    @Override
    public User addUser(User user) {
        validateUserInput(user);
        if (userMapper.selectByUsername(user.getUsername()) != null) {
            throw new BusinessException("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 管理员可指定任意角色，未指定默认学生
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("student");
        }
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);
        user.setPassword(null);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == null || userMapper.selectById(user.getId()) == null) {
            throw new BusinessException("用户不存在");
        }
        userMapper.update(user);
        User updated = userMapper.selectById(user.getId());
        updated.setPassword(null);
        return updated;
    }

    @Override
    public void deleteUser(Long id) {
        if (userMapper.selectById(id) == null) {
            throw new BusinessException("用户不存在");
        }
        userMapper.deleteById(id);
    }

    /** 注册/新增用户的字段校验 */
    private void validateUserInput(User user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new BusinessException("用户名不能为空");
        }
        if (user.getUsername().length() < 4 || user.getUsername().length() > 16) {
            throw new BusinessException("用户名长度需在4-16位之间");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new BusinessException("密码不能为空");
        }
        if (user.getPassword().length() < 6) {
            throw new BusinessException("密码至少6位");
        }
        if (user.getRealName() == null || user.getRealName().isBlank()) {
            throw new BusinessException("姓名不能为空");
        }
    }
}
