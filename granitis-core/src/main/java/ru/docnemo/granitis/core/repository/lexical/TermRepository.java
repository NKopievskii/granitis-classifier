package ru.docnemo.granitis.core.repository.lexical;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.docnemo.granitis.core.domain.lexical.TermDb;

import java.util.List;
import java.util.Optional;

public interface TermRepository extends JpaRepository<TermDb, Long> {

    Optional<TermDb> findByComponentsLexemeLexemeEqualsIgnoreCase(String lexeme);
    List<TermDb> findAllByComponentsLexemeLexemeInIgnoreCase(List<String> lexeme);

    Optional<TermDb> findByPartOfSpeechTraitAndComponentsLexemeLexemeEquals(String subclass, String lexeme);
    Optional<TermDb> findByComponentsLexemeLexemeEquals(String lexeme);
}
