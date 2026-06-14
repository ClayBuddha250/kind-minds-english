package org.example.kindmindsenglish.config;

import org.example.kindmindsenglish.config.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
     * 安全过滤链。
     * 1. 关闭 CSRF（前后端分离 + JWT 无需此防护）。
     * 2. 设置无状态会话（不创建 HttpSession）。
     * 3. 放行注册、登录、刷新令牌接口。
     * 4. 其余所有请求都需要认证。
     * 5. 将自定义的 JWT 过滤器添加到 UsernamePasswordAuthenticationFilter 之前。
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) {
        http
                // 关闭 CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // 无状态会话
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 401认证失败错误处理
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(401);
                            response.getWriter().write("{\"code\":401,\"message\":\"未认证或 token 无效\",\"data\":null}");
                        })
                )
                // 请求授权
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()   // 注册、登录、刷新放行
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")   // 只有管理员能访问
                        .anyRequest().authenticated()                     // 其他全部需要认证
                )
                // 将 JWT 过滤器放在 UsernamePasswordAuthenticationFilter 之前
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
