package ru.docnemo.granitis.semsyn.buildmssr.frame;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerbPrepositionFrame {
    private String situationMeaning;
    private String verbForm;
    private String verbReflect;
    private String verbVoice;
    private String prepositionTerm;
    private String caseTrait;
    private String frameMeaning;
    private String sort;
    private String comment;
}
