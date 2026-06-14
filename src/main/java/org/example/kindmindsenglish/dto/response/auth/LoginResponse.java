package org.example.kindmindsenglish.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>
 * 登录响应体。登录成功后返回给前端。
 * </p>
 *
 * @author ClayBuddha250
 * @since 2026-06-14
 */
@Data
@AllArgsConstructor
public class LoginResponse {
    private String accessToken; // 访问令牌
    private String refreshToken; // 刷新令牌
    private long expiresIn; // 访问令牌有效时间
}
