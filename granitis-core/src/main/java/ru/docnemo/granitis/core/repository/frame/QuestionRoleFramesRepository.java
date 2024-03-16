package ru.docnemo.granitis.core.repository.frame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.docnemo.granitis.core.domain.frame.QuestionRoleFrameDb;

import java.util.Optional;

public interface QuestionRoleFramesRepository extends JpaRepository<QuestionRoleFrameDb, Long>, JpaSpecificationExecutor<QuestionRoleFrameDb> {

    Optional<QuestionRoleFrameDb> findByPrepositionTermDbLexemesLexemeContainsAndQuestionWordTermDbLexemesLexemeContains(
            String prepositionTerm,
            String pronounInterrogativeRelativeAdverbTerm
    );
    Optional<QuestionRoleFrameDb> findByQuestionWordTermDbLexemesLexemeContains(
            String pronounInterrogativeRelativeAdverbTerm
    );
}
