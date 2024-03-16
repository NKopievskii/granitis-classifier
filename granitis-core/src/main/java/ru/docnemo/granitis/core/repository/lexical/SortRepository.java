package ru.docnemo.granitis.core.repository.lexical;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.docnemo.granitis.core.domain.meaning.SortDb;

import java.util.Optional;

public interface SortRepository extends JpaRepository<SortDb, Long> {
    Optional<SortDb> findBySort(String sort);
}
