package ru.docnemo.granitis.core.repository.lexical;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.docnemo.granitis.core.domain.meaning.MeaningType;

import java.util.Optional;

public interface MeaningTypeRepository extends JpaRepository<MeaningType, Long> {
    Optional<MeaningType> findByType(String type);
}
