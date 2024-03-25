package ru.docnemo.granitis.converter;

import ru.docnemo.granitis.core.domain.frame.QuestionRoleFrameDb;
import ru.docnemo.granitis.semsyn.buildmssr.frame.QuestionRoleFrame;

public class QuestionRoleFrameConverter implements Converter {
    public QuestionRoleFrame convert(QuestionRoleFrameDb questionRoleFrameDb) {
        return new QuestionRoleFrame(
                buildTerm(questionRoleFrameDb.getPrepositionTermDb()),
                buildTerm(questionRoleFrameDb.getQuestionWordTermDb()),
                questionRoleFrameDb.getFrameMeaning().getMeaning(),
                questionRoleFrameDb.getComment()
        );
    }
}
