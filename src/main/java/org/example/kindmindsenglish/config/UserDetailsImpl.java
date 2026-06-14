package org.example.kindmindsenglish.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * <p>
 * 自定义的 Spring Security 用户详情实现，包装 JWT 中提取的用户信息。
 * 实现 UserDetails 接口，以便与 Spring Security 无缝集成。
 * </p>
 *
 * @param userId 用户 ID
 * @param email  用户邮箱，作为登录凭证
 * @param role   用户角色：0 = 普通用户，1 = 管理员
 * @author ClayBuddha250
 * @since 2026-06-14
 */
public record UserDetailsImpl(Long userId, String email, Integer role) implements UserDetails {

    /**
     * 构造函数。
     *
     * @param userId 用户 ID
     * @param email  用户邮箱
     * @param role   用户角色
     */
    public UserDetailsImpl {
    }

    /**
     * 返回用户的权限集合。
     * Spring Security 通过角色前缀 "ROLE_" 进行鉴权，因此：
     * - 普通用户 (role=0) → ROLE_USER
     * - 管理员 (role=1) → ROLE_ADMIN
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = (role == 1) ? "ADMIN" : "USER";
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleName));
    }

    /**
     * JWT 认证无需密码，返回 null。
     */
    @Override
    public String getPassword() {
        return null;
    }

    /**
     * 使用邮箱作为用户名，作为用户唯一标识。
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * 账户是否未过期。当前统一返回 true，后续可通过 status 字段控制。
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否未被锁定。当前统一返回 true。
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 凭据是否未过期。当前统一返回 true。
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否启用。当前统一返回 true，后续可结合 status 字段实现禁用。
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * 获取用户id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 获取用户邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 获取用户角色
     */
    public Integer getRole() {
        return role;
    }
}
