package com.dormrepair;

import com.dormrepair.utils.JwtUtils;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * JWT 工具类单元测试（无需数据库，纯内存）。
 * 验证令牌的生成与解析能正确往返。
 */
class JwtUtilsTest {

    @Test
    void generateAndParseToken_roundTrip() {
        String secret = "dorm-repair-test-secret-key-at-least-32-bytes";
        JwtUtils jwtUtils = new JwtUtils(secret, 12);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "admin");
        claims.put("username", "admin");

        String token = jwtUtils.generateToken("1", claims);
        assertNotNull(token);

        var parsed = jwtUtils.parseToken(token);
        assertEquals("1", parsed.getSubject());
        assertEquals("admin", parsed.get("role", String.class));
        assertEquals("admin", parsed.get("username", String.class));
    }
}
