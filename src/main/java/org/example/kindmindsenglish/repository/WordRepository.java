package org.example.kindmindsenglish.repository;

import org.example.kindmindsenglish.entity.User;
import org.example.kindmindsenglish.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * <p>
 * 单词数据访问层，提供对 {@code word} 表的基础 CRUD 和自定义查询。
 * 继承 {@link JpaRepository} 后自动获得增删改查、分页、排序等基本能力，
 * 无需手动编写实现类。
 * </p>
 *
 * @author ClayBuddha250
 * @since 2026-06-13
 * @see User
 */
public interface WordRepository extends JpaRepository<Word, Long> {
    /**
     * 根据单词原型查找单词。
     * 用于：创建单词时检测重复等等
     *
     * @param word 单词原型，不能为 null
     * @return Optional<Word>，如果找到则包含单词实体，否则为 Optional.empty()
     */
    Optional<Word> findByWord(String word);
}
