package org.example.kindmindsenglish.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.kindmindsenglish.config.UserDetailsImpl;
import org.example.kindmindsenglish.util.JwtUtil;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * <p>
 * JWT 认证过滤器，继承 OncePerRequestFilter 确保每次请求只执行一次。
 * 该过滤器负责从请求头中提取 JWT，验证有效性，并将用户信息设置到 Spring Security 的上下文中，
 * 从而实现无状态的 JWT 认证。
 * </p>
 *
 * @author ClayBuddha250
 * @since 2026-06-14
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /** JWT 工具类，用于解析和验证令牌 */
    private final JwtUtil jwtUtil;

    /**
     * 构造函数注入 JwtUtil。
     *
     * @param jwtUtil JWT 工具类实例
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 过滤器的核心方法：在请求被处理之前执行。
     * 1. 从 Authorization 头中提取 Bearer Token。
     * 2. 验证 Token 并解析用户信息。
     * 3. 将用户信息封装为 Authentication 对象，存入 SecurityContextHolder。
     * 之后 Spring Security 就能随时获取当前登录用户。
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // 获取请求头中的 Authorization 字段
        String authHeader = request.getHeader("Authorization");

        // 检查是否包含 Bearer 令牌（标准格式：Bearer <token>）
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // 截取真正的 JWT 字符串（去掉前7个字符 "Bearer "）
            String token = authHeader.substring(7);

            // 验证令牌（签名正确且未过期）
            if (jwtUtil.validateToken(token)) {
                // 从令牌中提取用户 ID、邮箱、角色
                Long userId = jwtUtil.getUserIdFromToken(token);
                String email = jwtUtil.getEmailFromToken(token);
                Integer role = jwtUtil.getRoleFromToken(token);

                // 构建 UserDetails 对象（Spring Security 需要的用户详情）
                UserDetails userDetails = new UserDetailsImpl(userId, email, role);

                // 构建认证令牌（已认证状态，因为令牌有效）
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,      // 主体（用户信息）
                                null,             // 凭证（JWT 无需密码）
                                userDetails.getAuthorities() // 权限列表
                        );

                // 将认证对象存入 SecurityContextHolder（ThreadLocal 容器）
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            // 如果令牌无效，不设置 Authentication，后续的授权检查将拒绝请求
            log.debug("JWTFilter校验令牌无效:{}", token);
        }

        // 无论是否成功解析令牌，都继续执行后续过滤器链
        filterChain.doFilter(request, response);
    }
}
