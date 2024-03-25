package ru.docnemo.granitis.dictionary;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.docnemo.granitis.converter.TermConverter;
import ru.docnemo.granitis.core.domain.lexical.TermDb;
import ru.docnemo.granitis.core.repository.lexical.TermRepository;
import ru.docnemo.granitis.semsyn.buildmssr.frame.Term;
import ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary.LexicalDictionary;
import ru.docnemo.granitis.semsyn.buildmssr.morph.GrammaticAnalyzer;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DefaultLexicalDictionary implements LexicalDictionary {
    private final TermRepository termRepository;
    private final GrammaticAnalyzer grammaticAnalyzer;
    private final TermConverter termConverter;

    @Override
    @Transactional(readOnly = true)
    public Term findTerm(String unit) {
        String lexeme = grammaticAnalyzer.getGrammaticalProperties(unit).getBaseForm();
        Optional<TermDb> termDb = termRepository.findByComponentsLexemeLexemeEqualsIgnoreCase(lexeme);
        return termConverter.convert(termDb.orElseThrow(() -> new RuntimeException("Не нашли терм")));
    }

    @Override
    @Transactional(readOnly = true)
    public Term findTerm(List<String> units) {
        String lexeme = grammaticAnalyzer.getGrammaticalProperties(units.getFirst()).getBaseForm();
        Optional<TermDb> termDb = termRepository.findByComponentsLexemeLexemeEqualsIgnoreCase(lexeme);
        Term term = termConverter.convert(termDb.orElseThrow(() -> new RuntimeException("Не нашли терм")));
        String lexemeString = String.join(
                " ",
                units
                        .stream()
                        .map(unit -> grammaticAnalyzer.getGrammaticalProperties(unit).getBaseForm())
                        .toList()
        );
        if (term.getTerm().equals(lexemeString)) {
            return term;
        }
        throw new RuntimeException("Не нашли терм");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Term> findTerms(String unit) {
        List<String> lexemes = grammaticAnalyzer.getGrammaticalProperties(unit).getAllBaseForm();
        List<TermDb> termDb = termRepository.findAllByComponentsLexemeLexemeInIgnoreCase(lexemes);
        return termDb.stream().map(termConverter::convert).toList();
    }
}
