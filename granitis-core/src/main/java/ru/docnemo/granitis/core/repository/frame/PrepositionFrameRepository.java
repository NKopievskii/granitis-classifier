package ru.docnemo.granitis.core.repository.frame;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.docnemo.granitis.core.domain.frame.PrepositionFrameDb;

import java.util.List;
import java.util.Set;

public interface PrepositionFrameRepository extends JpaRepository<PrepositionFrameDb, Long> {
    List<PrepositionFrameDb> findAllByPrepositionTermDbLexemesLexemeContainsAndNounSort1SortInAndNounSort2SortInAndCaseTraitNoun2TraitIn(
            String preposition,
            Set<String> noun1Sorts,
            Set<String> noun2Sorts,
            Set<String> noun2GrCases
    );
    List<PrepositionFrameDb> findAllByNounSort1SortInAndNounSort2SortIn(
            Set<String> noun1Sorts,
            Set<String> noun2Sorts
    );
}
