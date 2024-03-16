package ru.docnemo.granitis.core.repository.lexical;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.docnemo.granitis.core.domain.lexical.MorphologicalTraitType;

import java.util.Optional;

public interface MorphTraitTypeRepository extends JpaRepository<MorphologicalTraitType, Long> {
    Optional<MorphologicalTraitType> findByTraitType(String traitType);
}
