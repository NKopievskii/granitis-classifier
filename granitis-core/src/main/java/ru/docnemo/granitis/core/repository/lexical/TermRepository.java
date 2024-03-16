package ru.docnemo.granitis.core.repository.lexical;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.docnemo.granitis.core.domain.lexical.TermDb;

import java.util.List;
import java.util.Optional;

public interface TermRepository extends JpaRepository<TermDb, Long> {

    Optional<TermDb> findByLexemesLexemeEqualsIgnoreCase(String lexeme);

    List<TermDb> findAllByLexemesLexemeEqualsIgnoreCase(String lexeme);
    List<TermDb> findAllByLexemesLexemeInIgnoreCase(List<String> lexeme);

    Optional<TermDb> findByPartOfSpeechTraitAndLexemesLexemeEquals(String subclass, String lexeme);
    Optional<TermDb> findByLexemesLexemeContains(String lexeme);
    Optional<TermDb> findByLexemesLexemeEquals(String lexeme);
}
