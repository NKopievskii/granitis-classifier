package ru.docnemo.granitis.core.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import ru.docnemo.granitis.core.domain.frame.VerbPrepositionFrameDb;
import ru.docnemo.granitis.core.domain.lexical.Lexeme;

import java.util.Set;

public class VerbPrepositionFrameSpecification {

    public static Specification<VerbPrepositionFrameDb> hasSituationMeaning(String meaning) {
        return (frame, query, builder) ->
                builder.equal(
                        frame.get("situationMeaning").get("meaning"),
                        meaning
                );
    }

    public static Specification<VerbPrepositionFrameDb> hasForm(String form) {
        return (frame, query, builder) ->
                builder.equal(
                        frame.get("verbForm").get("trait"),
                        form
                );
    }

    public static Specification<VerbPrepositionFrameDb> hasReflect(String reflect) {
        return (frame, query, builder) ->
                builder.equal(
                        frame.get("verbReflect").get("trait"),
                        reflect
                );
    }

    public static Specification<VerbPrepositionFrameDb> hasVoice(String voice) {
        return (frame, query, builder) ->
                builder.equal(
                        frame.get("verbVoice").get("trait"),
                        voice
                );
    }

    public static Specification<VerbPrepositionFrameDb> hasPreposition(String preposition) {
        return (frame, query, builder) -> {
            Join<VerbPrepositionFrameDb, Lexeme> join = frame.join("prepositionTermDb").join("lexemes");
            return builder.equal(
                    join.get("lexeme"),
                    preposition
            );
        };
    }

    public static Specification<VerbPrepositionFrameDb> containsCaseIn(Set<String> cases) {
        return (frame, query, builder) ->
                frame.get("caseTrait").get("trait").in(cases);
    }

    public static Specification<VerbPrepositionFrameDb> containsSortIn(Set<String> sorts) {
        return (frame, query, builder) ->
                frame.get("sort").get("sort").in(sorts);
    }
}
