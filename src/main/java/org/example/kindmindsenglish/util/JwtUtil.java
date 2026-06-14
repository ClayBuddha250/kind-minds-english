package org.example.kindmindsenglish.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * <p>
 * JWT 工具类，负责令牌的生成、解析和校验。
 * </p>
 *
 * @author ClayBuddha250
 * @since 2026-06-14
 */
@Component
public class JwtUtil {

    private final SecretKey secretKey; // 签名密钥,用于生成和验证 JWT 的签名
    private final long expiration; // accessToken 有效期,控制登录成功后,返回的 accessToken 在多长时间内有效
    private final long refreshExpiration; // refreshToken 有效期,控制 refreshToken 的存活时间，用于在 accessToken 过期后免密续签

    /**
     * 从配置文件注入密钥和有效期。
     */
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") long expiration,
                   @Value("${jwt.refresh-expiration}") long refreshExpiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
        this.refreshExpiration = refreshExpiration;
    }

    /**
     * 生成访问令牌（AccessToken）。
     *
     * @param userId   用户 ID
     * @param email    用户邮箱
     * @param role     用户角色
     * @return JWT 字符串
     */
    public String generateAccessToken(Long userId, String email, Integer role) {
        return Jwts.builder()
                .subject(email)                           // 主题存邮箱
                .claim("userId", userId)                  // 自定义载荷：用户 ID
                .claim("role", role)                      // 自定义载荷：角色
                .issuedAt(new Date())                     // 签发时间
                .expiration(new Date(System.currentTimeMillis() + expiration)) // 过期时间
                .signWith(secretKey)                      // 签名
                .compact();
    }

    /**
     * 生成刷新令牌（RefreshToken），有效期更长。
     *
     * @param userId 用户 ID
     * @param email  用户邮箱
     * @return JWT 字符串
     */
    public String generateRefreshToken(Long userId, String email) {
        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(secretKey)
                .compact();
    }

    /**
     * 获取accessToken令牌有效时间
     *
     * @return accessToken令牌有效时间
     */
    public long getAccessTokenExpirationSeconds() {
        return expiration / 1000;
    }

    /**
     * 从令牌中提取邮箱（Subject）。
     */
    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * 从令牌中提取用户 ID。
     */
    public Long getUserIdFromToken(String token) {
        return getClaims(token).get("userId", Long.class);
    }

    /**
     * 从令牌中提取用户角色。
     */
    public Integer getRoleFromToken(String token) {
        return getClaims(token).get("role", Integer.class);
    }

    /**
     * 校验令牌是否有效（签名正确且未过期）。
     *
     * @return true=有效，false=无效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 解析令牌的载荷。
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
