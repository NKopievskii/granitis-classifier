package ru.docnemo.granitis.grammatic.analyzer;

import company.evo.jmorphy2.Grammeme;
import company.evo.jmorphy2.MorphAnalyzer;
import company.evo.jmorphy2.ParsedWord;
import company.evo.jmorphy2.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.docnemo.granitis.core.domain.lexical.TermDb;
import ru.docnemo.granitis.core.repository.lexical.TermRepository;
import ru.docnemo.granitis.semsyn.buildmssr.morph.GrammaticAnalyzer;
import ru.docnemo.granitis.semsyn.buildmssr.morph.GrammaticalProperties;
import ru.docnemo.granitis.semsyn.buildmssr.morph.GrammaticalTag;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class DefaultGrammaticAnalyzer implements GrammaticAnalyzer {
    private final MorphAnalyzer morphAnalyzer;
    private final TermRepository termRepository;

    public GrammaticalProperties getGrammaticalProperties(String word) {
        try {
//            List<Tag> tags = morphAnalyzer.tag(word);
            List<ParsedWord> parsedWords = morphAnalyzer.parse(word);
            ParsedWord parsedWord1 = parsedWords
                    .stream()
                    .filter(parsedWord -> parsedWord.tag.POS.russianValue.equals("прил") && (!Objects.nonNull(parsedWord.tag.gender) || parsedWord.tag.gender.russianValue.equals("мр")))
                    .findFirst()
                    .orElse(
                            parsedWords.getFirst()
                    );

            String baseForm = parsedWord1.normalForm;
            Tag tag1 = parsedWord1.tag;
//            log.debug("tags {}, base forms {}", tags.size(), baseForm.size());
//            List<GrammaticalTag> grammaticalTags = tags
            List<GrammaticalTag> grammaticalTags = Stream.of(tag1)
                    .map(
                            tag -> GrammaticalTag
                                    .builder()
                                    .baseForm(baseForm) // todo переделать
//                                    .partOfSpeech(getRussianValue(tag.POS))
                                    .partOfSpeech(getPos(baseForm))
                                    .subclassPartOfSpeech(getSubclass(getRussianValue(tag.POS), baseForm))
                                    .animacy(getRussianValue(tag.anymacy))
                                    .gender(getRussianValue(tag.gender))
                                    .number(getRussianValue(tag.number))
                                    .grammaticalCase(getRussianValue(tag.Case))
                                    .aspect(getRussianValue(tag.aspect))
                                    .transitivity(getRussianValue(tag.transitivity))
                                    .person(getRussianValue(tag.person))
                                    .tense(getRussianValue(tag.tense))
                                    .mood(
                                            Objects.requireNonNullElse(getRussianValue(tag.mood), "неопр")
                                    )
                                    .voice(getRussianValue(tag.voice))
                                    .reflect(
                                            tag.containsAnyValues(List.of("Refl"))
                                            ? "возвр"
                                            : "невозвр"
                                    )
                                    .involvement(getRussianValue(tag.involvement))
                                    .build()
                    )
                    .toList();
            return new GrammaticalProperties(grammaticalTags);  // переделать на работу со списком или определние нужного
        } catch (IOException e) {
            log.error("Не удалось определить грамматические свойства: ", e);
            throw new RuntimeException(e); // todo нормальный ексепшн
        } catch (NullPointerException e) {
            log.error("Слово {} проблемное", word, e);
            throw new RuntimeException(e); // todo нормальный ексепшн
        }
    }

    String getSubclass(String pos, String lexeme) {
        Optional<TermDb> termDb = termRepository.findByPartOfSpeechTraitAndLexemesLexemeEquals(pos, lexeme);
        if (termDb.isPresent()) {
            return termDb.get().getSubclass().getTrait();
        }
        return "";
    }

    String getPos(String lexeme) {
        Optional<TermDb> termDb = termRepository.findByLexemesLexemeEquals(lexeme);
        if (termDb.isPresent()) {
            return termDb.get().getPartOfSpeech().getTrait();
        }
        return "";
    }


    String getRussianValue(Grammeme grammeme) {
        return Objects.nonNull(grammeme)
                ? grammeme.russianValue
                : null;
    }
}
