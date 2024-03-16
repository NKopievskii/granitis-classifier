package ru.docnemo.granitis.converter;

import ru.docnemo.granitis.core.domain.frame.VerbPrepositionFrameDb;
import ru.docnemo.granitis.core.domain.lexical.MorphologicalTrait;
import ru.docnemo.granitis.core.domain.lexical.TermDb;
import ru.docnemo.granitis.semsyn.buildmssr.frame.VerbPrepositionFrame;

import java.util.Objects;

public class VerbPrepositionFrameConverter {
    public VerbPrepositionFrame convert(VerbPrepositionFrameDb frameDb) {
        return new VerbPrepositionFrame(
                frameDb.getSituationMeaning().getMeaning(),
                getTrait(frameDb.getVerbForm()),
                getTrait(frameDb.getVerbReflect()),
                getTrait(frameDb.getVerbVoice()),
                getLexeme(frameDb.getPrepositionTermDb()),
                getTrait(frameDb.getCaseTrait()),
                frameDb.getFrameMeaning().getMeaning(),
                frameDb.getSort().getSort(),
                frameDb.getComment()
        );
    }

    String getTrait(MorphologicalTrait value) {
        return Objects.nonNull(value)
                ? value.getTrait()
                : null;
    }

    String getLexeme(TermDb value) {
        return Objects.nonNull(value)
                ? value.getLexemes().getFirst().getLexeme()
                : null;
    }
}
