package ru.docnemo.granitis.semsyn.buildmssr.frame;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrepositionFrame {
    private String prepositionTerm;
    private String additionalMeaningNoun1;
    private String additionalMeaningNoun2;
    private String caseTraitNoun2;
    private String frameMeaning;
    private String comment;
}
