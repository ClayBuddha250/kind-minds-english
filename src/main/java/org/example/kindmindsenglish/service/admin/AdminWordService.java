package org.example.kindmindsenglish.service.admin;

import lombok.extern.slf4j.Slf4j;
import org.example.kindmindsenglish.dto.request.admin.WordCreateRequest;
import org.example.kindmindsenglish.dto.response.admin.WordResponse;
import org.example.kindmindsenglish.entity.Word;
import org.example.kindmindsenglish.exception.BusinessException;
import org.example.kindmindsenglish.repository.WordRepository;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员单词业务逻辑层，负责处理与单词相关的所有操作。
 * 包括创建单词、修改单词、删除单词等。
 * </p>
 *
 * @author ClayBuddha250
 * @since 2026-06-14
 * @see WordRepository
 */
@Slf4j
@Service
public class AdminWordService {
    private final WordRepository wordRepository;

    public AdminWordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    /**
     * 创建新单词。
     * 校验单词是否不为空。
     *
     * @param wordCreateRequest 单词信息，不能为空
     * @return 保存成功的单词实体（含自增ID）
     * @throws BusinessException 创建单词重复时抛出
     */
    public WordResponse createNewWord(WordCreateRequest wordCreateRequest) {
        // 检查单词唯一性
        String lemma = wordCreateRequest.getWord();
        Word word = wordRepository.findByWord(lemma).orElse(null);
        if (word != null) {
            log.warn("单词原型已经存在,不允许重复创建");
            throw new BusinessException(20001, "单词重复创建");
        }

        // 组装保存单词
        word = new Word();
        word.setWord(lemma);
        word.setTranslation(wordCreateRequest.getTranslation());
        word.setPhonetic(wordCreateRequest.getPhonetic());
        word.setDefinition(wordCreateRequest.getDefinition());
        word.setPos(wordCreateRequest.getPos());
        word.setCollins(wordCreateRequest.getCollins());
        word.setOxford(wordCreateRequest.getOxford());
        word.setTag(wordCreateRequest.getTag());
        word.setBnc(wordCreateRequest.getBnc());
        word.setFrq(wordCreateRequest.getFrq());
        word.setExchange(wordCreateRequest.getExchange());
        word.setDetail(wordCreateRequest.getDetail());
        word.setAudio(wordCreateRequest.getAudio());
        Word saveWord = wordRepository.save(word);

        // 组装响应体
        WordResponse wordResponse = new WordResponse();
        wordResponse.setId(saveWord.getId());
        wordResponse.setWord(saveWord.getWord());
        wordResponse.setTranslation(saveWord.getTranslation());
        wordResponse.setPhonetic(saveWord.getPhonetic());
        wordResponse.setDefinition(saveWord.getDefinition());
        wordResponse.setPos(saveWord.getPos());
        wordResponse.setCollins(saveWord.getCollins());
        wordResponse.setOxford(saveWord.getOxford());
        wordResponse.setTag(saveWord.getTag());
        wordResponse.setBnc(saveWord.getBnc());
        wordResponse.setFrq(saveWord.getFrq());
        wordResponse.setExchange(saveWord.getExchange());
        wordResponse.setDetail(saveWord.getDetail());
        wordResponse.setAudio(saveWord.getAudio());
        wordResponse.setCreatedAt(saveWord.getCreatedAt());
        wordResponse.setUpdatedAt(saveWord.getUpdatedAt());

        // 返回单词
        log.info("添加单词: {}", lemma);
        return wordResponse;
    }
}
