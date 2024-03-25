package ru.docnemo.granitis.core.repository.frame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.docnemo.granitis.core.domain.frame.QuestionRoleFrameDb;


public interface QuestionRoleFrameRepository extends JpaRepository<QuestionRoleFrameDb, Long>, JpaSpecificationExecutor<QuestionRoleFrameDb> {

}
