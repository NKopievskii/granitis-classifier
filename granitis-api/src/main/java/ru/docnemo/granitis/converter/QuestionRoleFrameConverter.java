package ru.docnemo.granitis.converter;

import ru.docnemo.granitis.core.domain.frame.QuestionRoleFrameDb;
import ru.docnemo.granitis.semsyn.buildmssr.frame.QuestionRoleFrame;

public class QuestionRoleFrameConverter {
    public QuestionRoleFrame convert(QuestionRoleFrameDb questionRoleFrameDb) {
        return new QuestionRoleFrame(
                questionRoleFrameDb.getPrepositionTermDb().getLexemes().getFirst().getLexeme(),
                questionRoleFrameDb.getQuestionWordTermDb().getLexemes().getFirst().getLexeme(),
                questionRoleFrameDb.getFrameMeaning().getMeaning(),
                questionRoleFrameDb.getComment()
        );
    }
}
