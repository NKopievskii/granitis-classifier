package ru.docnemo.granitis.dictionary;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.docnemo.granitis.core.domain.meaning.SortDb;
import ru.docnemo.granitis.core.repository.lexical.SortRepository;
import ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary.SortDictionary;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
public class DefaultSortDictionary implements SortDictionary {
    private final SortRepository sortRepository;
    @Override
    @Transactional(readOnly = true)
    public Set<String> sortSpectre(String sort) {
        SortDb sortDbRoot = sortRepository
                .findBySort(sort)
                .orElseThrow(() -> new RuntimeException("Не найден сорт")); // todo нормальный эксепшн

        Set<String> spectre = new HashSet<>();
        spectre.add(sortDbRoot.getSort());
        while (Objects.nonNull(sortDbRoot.getHyperonim())) {
            sortDbRoot = sortDbRoot.getHyperonim();
            spectre.add(sortDbRoot.getSort());
        }

        return spectre;
    }
}
