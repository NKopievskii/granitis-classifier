package ru.docnemo.granitis.core.repository.meanings;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.docnemo.granitis.core.domain.meaning.DomainDb;

public interface DomainRepository extends JpaRepository<DomainDb, Long> {
}
