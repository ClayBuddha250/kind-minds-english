package org.example.kindmindsenglish.service;

import lombok.extern.slf4j.Slf4j;
import org.example.kindmindsenglish.dto.response.auth.LoginResponse;
import org.example.kindmindsenglish.dto.response.user.MeResponse;
import org.example.kindmindsenglish.entity.User;
import org.example.kindmindsenglish.exception.BusinessException;
import org.example.kindmindsenglish.repository.UserRepository;
import org.example.kindmindsenglish.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户业务逻辑层，负责处理与用户相关的所有操作。
 * 包括注册、登录验证、用户信息更新等。
 * </p>
 *
 * @author ClayBuddha250
 * @since 2026-06-14
 * @see UserRepository
 * @see PasswordEncoder
 */
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 构造函数注入，Spring 会自动装配对应的 Bean。
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 注册新用户。
     * 校验邮箱是否已存在，将明文密码加密后保存。
     *
     * @param username    用户显示名，不能为空
     * @param email       登录邮箱，不能为空且唯一
     * @param rawPassword 明文密码，不能为空（至少6位由 Controller 校验）
     * @return 保存成功的用户实体（含自增ID）
     * @throws BusinessException 邮箱已被注册时抛出
     */
    public User register(String username, String email, String rawPassword) {
        // 检查邮箱唯一性
        if (userRepository.existsByEmail(email)) {
            log.warn("注册失败：邮箱已被注册，email={}", email);
            throw new BusinessException(10001, "邮箱已被注册");
        }

        // 组装用户对象
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(0);   // 0 = 普通用户
        user.setStatus(1); // 1 = 有效用户

        // 持久化并返回
        User savedUser = userRepository.save(user);
        log.info("用户注册成功：id={}, username={}, email={}", savedUser.getId(), username, email);
        return savedUser;
    }

    /**
     * 登录用户。
     * 校验邮箱是否已存在，将明文密码加密后比较。
     *
     * @param email       登录邮箱，不能为空且唯一
     * @param rawPassword 明文密码，不能为空（至少6位由 Controller 校验）
     * @return 包含 accessToken、refreshToken、expiresIn 的响应对象
     * @throws BusinessException 邮箱未被注册、密码不正确、账户不可用时抛出
     */
    public LoginResponse login(String email, String rawPassword) {
        // 检查邮箱正确性
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            log.warn("登录失败，邮箱不正确:email={}", email);
            throw new BusinessException(10002,"邮箱或密码不正确");
        }

        // 检查密码正确性
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            log.warn("登录失败，密码不正确");
            throw new BusinessException(10002,"邮箱或密码不正确");
        }

        // 检查账号状态
        if (user.getStatus() == 0) {
            log.warn("登录失败，账号已禁用：email={}", email);
            throw new BusinessException(10003, "账户已被禁用");
        }

        // 生成令牌
        String accessToken = jwtUtil.generateAccessToken(
                user.getId(), user.getEmail(), user.getRole()
        );
        String refreshToken = jwtUtil.generateRefreshToken(
                user.getId(), user.getEmail()
        );

        log.info("用户登录成功：id={}, email={}", user.getId(), email);
        return new LoginResponse(accessToken, refreshToken, jwtUtil.getAccessTokenExpirationSeconds());
    }

    /**
     * 获取当前登录用户的详细信息。
     *
     * @param userId 当前用户 ID（从 JWT 中提取）
     * @return 用户信息响应体
     * @throws BusinessException 用户不存在时抛出
     */
    public MeResponse getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(10005, "用户不存在"));

        MeResponse response = new MeResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
