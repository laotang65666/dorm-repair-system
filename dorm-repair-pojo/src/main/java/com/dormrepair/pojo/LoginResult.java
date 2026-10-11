package com.dormrepair.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录返回结果：token（令牌）+ user（用户信息）
 * 前端拿到 token 后，后续请求都放在请求头 Authorization 里。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResult {
    private String token;
    private User user;
}
