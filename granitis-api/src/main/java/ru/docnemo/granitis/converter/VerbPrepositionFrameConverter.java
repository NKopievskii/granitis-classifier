package ru.docnemo.granitis.converter;

import ru.docnemo.granitis.core.domain.frame.VerbPrepositionFrameDb;
import ru.docnemo.granitis.core.domain.lexical.MorphologicalTrait;
import ru.docnemo.granitis.semsyn.buildmssr.frame.VerbPrepositionFrame;

import java.util.Objects;

public class VerbPrepositionFrameConverter implements Converter {
    public VerbPrepositionFrame convert(VerbPrepositionFrameDb frameDb) {
        return new VerbPrepositionFrame(
                frameDb.getSituationMeaning().getMeaning(),
                getTrait(frameDb.getVerbForm()),
                getTrait(frameDb.getVerbReflect()),
                getTrait(frameDb.getVerbVoice()),
                Objects.nonNull(frameDb.getPrepositionTermDb())
                        ? buildTerm(frameDb.getPrepositionTermDb())
                        : null,
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
}
