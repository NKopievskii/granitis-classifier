package ru.docnemo.granitis.converter;

import ru.docnemo.granitis.core.domain.frame.PrepositionFrameDb;
import ru.docnemo.granitis.semsyn.buildmssr.frame.PrepositionFrame;

public class PrepositionFrameConverter {
    public PrepositionFrame convert(PrepositionFrameDb prepositionFrameDb) {
        return new PrepositionFrame(
                prepositionFrameDb.getPrepositionTermDb().getLexemes().getFirst().getLexeme(),
                prepositionFrameDb.getNounSort1().getSort(),
                prepositionFrameDb.getNounSort2().getSort(),
                prepositionFrameDb.getCaseTraitNoun2().getTrait(),
                prepositionFrameDb.getFrameMeaning().getMeaning(),
                prepositionFrameDb.getComment()
        );
    }
}
