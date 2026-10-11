package com.dormrepair.common;

import com.dormrepair.pojo.User;
import com.dormrepair.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Token 服务：负责「发 token、查 token」。
 *
 * 基于 JWT 实现（无状态，不占服务器内存）：
 *   - 登录时把用户信息（id、role、姓名等）放进 JWT 载荷，签名后返回给前端；
 *   - 后续请求前端带着 token 来，这里解析校验并还原出「轻量用户对象」。
 */
@Component
public class TokenService {

    private final JwtUtils jwtUtils;

    public TokenService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    /** 登录成功后：把用户信息塞进 JWT，生成 token 返回 */
    public String login(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());
        claims.put("realName", user.getRealName());
        return jwtUtils.generateToken(String.valueOf(user.getId()), claims);
    }

    /** 根据 token 还原用户（解析失败或过期返回 null，即「未登录」） */
    public User getUser(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        try {
            Claims claims = jwtUtils.parseToken(token);
            User user = new User();
            user.setId(toLong(claims.get("id")));
            user.setUsername(claims.get("username", String.class));
            user.setRole(claims.get("role", String.class));
            user.setRealName(claims.get("realName", String.class));
            return user;
        } catch (Exception e) {
            // 签名不对 / 已过期 / 格式错误，都视为「未登录」
            return null;
        }
    }

    /** 把 JWT 载荷里的数字安全转成 Long（避免 Integer/Long 类型不匹配） */
    private static Long toLong(Object value) {
        return value == null ? null : Long.valueOf(value.toString());
    }
}
