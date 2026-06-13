package org.example.kindmindsenglish.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * <p>
 * Spring Security 核心配置。
 * 当前阶段仅开启密码编码器，并放行认证相关接口，保证注册功能可调通。
 * 后续加入 JWT 后再逐步收紧安全策略。
 * </p>
 *
 * @author ClayBuddha250
 * @since 2026-06-14
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 密码编码器，使用 BCrypt 算法。
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 安全过滤链（临时配置）。
     * 放行所有 /api/v1/auth/** 下的请求，允许注册和登录。
     * 后续引入 JWT 后再改成仅对部分路径进行认证。
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
                // 临时关闭 CSRF（开发阶段，否则 POST 请求会被拦截）
                .csrf(AbstractHttpConfigurer::disable)
                // 配置请求授权
                .authorizeHttpRequests(auth -> auth
                        // 放行认证相关接口
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        // 其他所有请求暂时也放行（后续改为需要认证）
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}
