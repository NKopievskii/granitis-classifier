package ru.docnemo.granitis.core.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import ru.docnemo.granitis.core.domain.frame.QuestionRoleFrameDb;
import ru.docnemo.granitis.core.domain.lexical.Lexeme;

import java.util.Objects;

public class QuestionRoleFrameSpecification {

    public static Specification<QuestionRoleFrameDb> hasPreposition(String preposition) {
        return (frame, query, builder) -> {
            if (Objects.isNull(preposition)) {
                return builder.equal(
                        frame.get("prepositionTermDb"),
                        (Object) null
                );
            }

            Join<QuestionRoleFrameDb, Lexeme> join = frame
                    .join("prepositionTermDb")
                    .join("components")
                    .join("lexeme");
            return builder.equal(
                    join.get("lexeme"),
                    preposition
            );
        };
    }

    public static Specification<QuestionRoleFrameDb> hasQuestionWord(String questionWord) {
        return (frame, query, builder) -> {
            Join<QuestionRoleFrameDb, Lexeme> join = frame
                    .join("questionWordTermDb")
                    .join("components")
                    .join("lexeme");
            return builder.equal(
                    join.get("lexeme"),
                    questionWord
            );
        };
    }

}
