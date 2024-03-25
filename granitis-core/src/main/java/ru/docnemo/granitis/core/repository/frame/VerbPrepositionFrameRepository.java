package ru.docnemo.granitis.core.repository.frame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.docnemo.granitis.core.domain.frame.VerbPrepositionFrameDb;

public interface VerbPrepositionFrameRepository extends JpaRepository<VerbPrepositionFrameDb, Long>,
        JpaSpecificationExecutor<VerbPrepositionFrameDb> {

}
