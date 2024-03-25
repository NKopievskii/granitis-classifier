package ru.docnemo.granitis.dictionary;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import ru.docnemo.granitis.converter.QuestionRoleFrameConverter;
import ru.docnemo.granitis.core.domain.frame.QuestionRoleFrameDb;
import ru.docnemo.granitis.core.repository.frame.QuestionRoleFrameRepository;
import ru.docnemo.granitis.core.specification.QuestionRoleFrameSpecification;
import ru.docnemo.granitis.semsyn.buildmssr.frame.QuestionRoleFrame;
import ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary.QuestionRoleFrameDictionary;

import java.util.Optional;

@RequiredArgsConstructor
public class DefaultQuestionRoleFrameDictionary implements QuestionRoleFrameDictionary {
    private final QuestionRoleFrameRepository questionRoleFrameRepository;
    private final QuestionRoleFrameConverter questionRoleFrameConverter;
    @Override
    @Transactional(readOnly = true)
    public QuestionRoleFrame findByPrepositionAndQuestionWord(String preposition, String word) {
        Specification<QuestionRoleFrameDb> specification = QuestionRoleFrameSpecification.hasPreposition(preposition);
        specification = specification.and(QuestionRoleFrameSpecification.hasQuestionWord(word));

        Optional<QuestionRoleFrameDb> questionRoleFrameDb = questionRoleFrameRepository.findOne(specification);

        return questionRoleFrameConverter.convert(questionRoleFrameDb.orElseThrow(() -> new RuntimeException("Не найден фрейм"))); // сделать нормальный экспешн
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionRoleFrame findByQuestionWord(String word) {
        return findByPrepositionAndQuestionWord(null, word);
    }
}
