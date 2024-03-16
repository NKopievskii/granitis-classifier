package ru.docnemo.granitis.core.repository.lexical;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.docnemo.granitis.core.domain.meaning.Meaning;

import java.util.Optional;

public interface MeaningRepository extends JpaRepository<Meaning, Long> {
    Optional<Meaning> findByMeaning(String meaning);
}
