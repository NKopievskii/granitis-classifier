package ru.docnemo.granitis.core.repository.lexical;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.docnemo.granitis.core.domain.lexical.MorphologicalTrait;

public interface MorphRepository extends JpaRepository<MorphologicalTrait, Long> {
}
