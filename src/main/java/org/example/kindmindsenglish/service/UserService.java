package org.example.kindmindsenglish.service;

import lombok.extern.slf4j.Slf4j;
import org.example.kindmindsenglish.entity.User;
import org.example.kindmindsenglish.exception.BusinessException;
import org.example.kindmindsenglish.repository.UserRepository;
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

    /**
     * 构造函数注入，Spring 会自动装配对应的 Bean。
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 注册新用户。
     * 校验邮箱是否已存在，将明文密码加密后保存。
     *
     * @param username    用户显示名，不能为空
     * @param email       登录邮箱，不能为空且唯一
     * @param rawPassword 明文密码，不能为空（至少6位由 Controller 校验）
     * @return 保存成功的用户实体（含自增ID）
     * @throws IllegalArgumentException 邮箱已被注册时抛出
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
}
