package ru.docnemo.granitis.dictionary;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.docnemo.granitis.converter.VerbPrepositionFrameConverter;
import ru.docnemo.granitis.core.domain.frame.VerbPrepositionFrameDb;
import ru.docnemo.granitis.core.repository.frame.VerbPrepositionFrameRepository;
import ru.docnemo.granitis.core.specification.VerbPrepositionFrameSpecification;
import ru.docnemo.granitis.semsyn.buildmssr.frame.VerbPrepositionFrame;
import ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary.VerbPrepositionFrameDictionary;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
public class DefaultVerbPrepositionFrameDictionary implements VerbPrepositionFrameDictionary {
    private final VerbPrepositionFrameRepository verbPrepositionFrameRepository;
    private final VerbPrepositionFrameConverter verbPrepositionFrameConverter;

    @Override
    public List<VerbPrepositionFrame> find(String situationMeaning, String form, String reflection, String voice, String preposition, Set<String> sorts, Set<String> grammaticalCases) {
        Specification<VerbPrepositionFrameDb> specification = VerbPrepositionFrameSpecification.hasSituationMeaning(situationMeaning);
        if (Objects.nonNull(form)) {
            specification = specification.and(VerbPrepositionFrameSpecification.hasForm(form));
        }
        if (Objects.nonNull(reflection)) {
            specification = specification.and(VerbPrepositionFrameSpecification.hasReflect(reflection));
        }
        if (Objects.nonNull(voice)) {
            specification = specification.and(VerbPrepositionFrameSpecification.hasVoice(voice));
        }
        if (Objects.nonNull(preposition)) {
            specification = specification.and(VerbPrepositionFrameSpecification.hasPreposition(preposition));
        }
        if (Objects.nonNull(sorts) && !sorts.isEmpty()) {
            specification = specification.and(VerbPrepositionFrameSpecification.containsSortIn(sorts));
        }
        if (Objects.nonNull(grammaticalCases) && !grammaticalCases.isEmpty()) {
            specification = specification.and(VerbPrepositionFrameSpecification.containsCaseIn(grammaticalCases));
        }

        List<VerbPrepositionFrameDb> frameDbs = verbPrepositionFrameRepository.findAll(specification);

        return frameDbs.stream().map(verbPrepositionFrameConverter::convert).toList();
    }

    @Override
    public List<VerbPrepositionFrame> findForConstruct(String situationMeaning, String prep) {
        return null;
    }
}
