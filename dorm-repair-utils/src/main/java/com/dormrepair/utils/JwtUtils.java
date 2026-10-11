package com.dormrepair.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT 令牌工具类：负责「生成令牌、解析校验令牌」。
 *
 * JWT 是「无状态」的：服务端不保存 token，靠签名密钥验证真伪，令牌本身携带用户信息。
 * 签名密钥从配置读取（jwt.secret），支持环境变量覆盖，不写死在代码里。
 */
@Component
public class JwtUtils {

    /** 签名密钥对象（由配置里的明文密钥生成，HS256 要求明文至少 32 字节） */
    private final SecretKey signKey;

    /** 令牌有效期（毫秒） */
    private final long expireMillis;

    public JwtUtils(@Value("${jwt.secret}") String secret,
                    @Value("${jwt.expire-hours:12}") long expireHours) {
        this.signKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expireMillis = expireHours * 60 * 60 * 1000;
    }

    /**
     * 生成 JWT 令牌
     * @param subject 主题（标准字段，通常存用户唯一 ID）
     * @param claims  自定义业务载荷，可存 id、role、realName 等
     * @return 三段式 JWT 字符串（header.payload.signature）
     */
    public String generateToken(String subject, Map<String, Object> claims) {
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expireMillis))
                .signWith(signKey)
                .compact();
    }

    /**
     * 解析并校验 JWT 令牌。
     * 自动完成格式校验、签名校验、过期校验；校验失败抛异常，调用方需捕获处理。
     * @param token 待解析的 JWT 字符串
     * @return 令牌载荷（可读取 id、role 等自定义字段）
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(signKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
