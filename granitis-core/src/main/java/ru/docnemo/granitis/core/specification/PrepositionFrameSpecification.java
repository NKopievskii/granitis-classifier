package ru.docnemo.granitis.core.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import ru.docnemo.granitis.core.domain.frame.PrepositionFrameDb;
import ru.docnemo.granitis.core.domain.lexical.Lexeme;

import java.util.Objects;
import java.util.Set;

public class PrepositionFrameSpecification {

    public static Specification<PrepositionFrameDb> hasPreposition(String preposition) {
        return (frame, query, builder) -> {
            if (Objects.isNull(preposition)) {
                return builder.equal(
                        frame.get("prepositionTermDb"),
                        preposition
                );
            }

            Join<PrepositionFrameDb, Lexeme> join = frame
                    .join("prepositionTermDb")
                    .join("components")
                    .join("lexeme");
            return builder.equal(
                    join.get("lexeme"),
                    preposition
            );
        };
    }

    public static Specification<PrepositionFrameDb> containsCaseIn(Set<String> cases) {
        return (frame, query, builder) ->
                frame.get("caseTraitNoun2").get("trait").in(cases);
    }

    public static Specification<PrepositionFrameDb> containsNoun1SortIn(Set<String> sorts) {
        return containsSortIn("nounSort1", sorts);
    }

    public static Specification<PrepositionFrameDb> containsNoun2SortIn(Set<String> sorts) {
        return containsSortIn("nounSort2", sorts);
    }

    public static Specification<PrepositionFrameDb> containsSortIn(String key, Set<String> sorts) {
        return (frame, query, builder) ->
                frame.get(key).get("sort").in(sorts);
    }

}
