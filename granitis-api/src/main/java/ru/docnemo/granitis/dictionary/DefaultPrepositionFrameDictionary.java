package ru.docnemo.granitis.dictionary;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import ru.docnemo.granitis.converter.PrepositionFrameConverter;
import ru.docnemo.granitis.core.domain.frame.PrepositionFrameDb;
import ru.docnemo.granitis.core.repository.frame.PrepositionFrameRepository;
import ru.docnemo.granitis.core.specification.PrepositionFrameSpecification;
import ru.docnemo.granitis.semsyn.buildmssr.frame.PrepositionFrame;
import ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary.PrepositionFrameDictionary;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class DefaultPrepositionFrameDictionary implements PrepositionFrameDictionary {
    private final PrepositionFrameRepository prepositionFrameRepository;
    private final PrepositionFrameConverter prepositionFrameConverter;

    @Override
    @Transactional(readOnly = true)
    public List<PrepositionFrame> find(String preposition, Set<String> noun1Sorts, Set<String> noun2Sorts, Set<String> noun2GrCases) {

        Specification<PrepositionFrameDb> specification = PrepositionFrameSpecification.hasPreposition(preposition);
        specification = specification.and(PrepositionFrameSpecification.containsNoun1SortIn(noun1Sorts));
        specification = specification.and(PrepositionFrameSpecification.containsNoun2SortIn(noun2Sorts));
        specification = specification.and(PrepositionFrameSpecification.containsCaseIn(noun2GrCases));

        List<PrepositionFrameDb> prepositionFrameDb = prepositionFrameRepository.findAll(specification);

        return prepositionFrameDb.stream().map(prepositionFrameConverter::convert).toList();
    }
}
