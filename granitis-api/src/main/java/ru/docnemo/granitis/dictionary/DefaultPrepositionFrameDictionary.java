package ru.docnemo.granitis.dictionary;

import lombok.RequiredArgsConstructor;
import ru.docnemo.granitis.converter.PrepositionFrameConverter;
import ru.docnemo.granitis.core.domain.frame.PrepositionFrameDb;
import ru.docnemo.granitis.core.repository.frame.PrepositionFrameRepository;
import ru.docnemo.granitis.semsyn.buildmssr.frame.PrepositionFrame;
import ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary.PrepositionFrameDictionary;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
public class DefaultPrepositionFrameDictionary implements PrepositionFrameDictionary {
    private final PrepositionFrameRepository prepositionFrameRepository;
    private final PrepositionFrameConverter prepositionFrameConverter;

    @Override
    public List<PrepositionFrame> find(String preposition, Set<String> noun1Sorts, Set<String> noun2Sorts, Set<String> noun2GrCases) {
        if (Objects.isNull(preposition)) {
            preposition = "nil"; // todo костыль, надо убрать
        }
        List<PrepositionFrameDb> prepositionFrameDb = prepositionFrameRepository.findAllByPrepositionTermDbLexemesLexemeContainsAndNounSort1SortInAndNounSort2SortInAndCaseTraitNoun2TraitIn(
                preposition,
                noun1Sorts,
                noun2Sorts,
                noun2GrCases
        );

        return prepositionFrameDb.stream().map(prepositionFrameConverter::convert).toList();
    }
}
