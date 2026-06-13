package org.example.kindmindsenglish.repository;

import org.example.kindmindsenglish.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <p>
 * 用户数据访问层，提供对 {@code user} 表的基础 CRUD 和自定义查询。
 * 继承 {@link JpaRepository} 后自动获得增删改查、分页、排序等基本能力，
 * 无需手动编写实现类。
 * </p>
 *
 * @author ClayBuddha250
 * @since 2026-06-13
 * @see User
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据邮箱查找用户。
     * 用于：登录时验证用户是否存在，注册时检查邮箱是否已被占用。
     *
     * @param email 用户邮箱，不能为 null
     * @return Optional<User>，如果找到则包含用户实体，否则为 Optional.empty()
     */
    Optional<User> findByEmail(String email);

    /**
     * 检查邮箱是否已被注册。
     * 用于：注册时的唯一性校验，比 findByEmail 更高效（只查存在性，不加载全部数据）。
     *
     * @param email 用户邮箱，不能为 null
     * @return true=邮箱已被占用，false=邮箱可用
     */
    boolean existsByEmail(String email);
}
