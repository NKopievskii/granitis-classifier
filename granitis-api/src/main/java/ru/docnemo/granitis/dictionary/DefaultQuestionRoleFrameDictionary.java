package ru.docnemo.granitis.dictionary;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.docnemo.granitis.converter.QuestionRoleFrameConverter;
import ru.docnemo.granitis.core.domain.frame.QuestionRoleFrameDb;
import ru.docnemo.granitis.core.repository.frame.QuestionRoleFramesRepository;
import ru.docnemo.granitis.semsyn.buildmssr.frame.QuestionRoleFrame;
import ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary.QuestionRoleFrameDictionary;

import java.util.Optional;

@RequiredArgsConstructor
public class DefaultQuestionRoleFrameDictionary implements QuestionRoleFrameDictionary {
    private final QuestionRoleFramesRepository questionRoleFramesRepository;
    private final QuestionRoleFrameConverter questionRoleFrameConverter;
    @Override
    @Transactional(readOnly = true)
    public QuestionRoleFrame findByPrepositionAndQuestionWord(String preposition, String word) {
        Optional<QuestionRoleFrameDb> questionRoleFrameDb = questionRoleFramesRepository.findByPrepositionTermDbLexemesLexemeContainsAndQuestionWordTermDbLexemesLexemeContains(
            preposition,
            word
        );
        return questionRoleFrameConverter.convert(questionRoleFrameDb.orElseThrow(() -> new RuntimeException("Не найден фрейм"))); // сделать нормальный экспешн
    }

    @Override
    public QuestionRoleFrame findByQuestionWord(String word) {
        Optional<QuestionRoleFrameDb> questionRoleFrameDb = questionRoleFramesRepository.findByQuestionWordTermDbLexemesLexemeContains(
                word
        );
        return questionRoleFrameConverter.convert(questionRoleFrameDb.orElseThrow(() -> new RuntimeException("Не найден фрейм"))); // сделать нормальный экспешн
    }
}
