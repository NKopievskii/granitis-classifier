package ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.specific;

import ru.docnemo.granitis.semsyn.buildmssr.morph.GrammaticAnalyzer;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.TextType;

public class GenQs extends AbstractSpecificTextTypeDeterminant {
    public GenQs(GrammaticAnalyzer grammaticAnalyzer) {
        super(grammaticAnalyzer);
    }

    @Override
    public boolean isThis(String[] text) {
        return grammaticAnalyzer.getGrammaticalProperties(text[0]).getPartOfSpeech().equals("глагол")
                && text[1].equals("ли");
    }

    @Override
    public Meta getMeta(String[] text) {
        return new Meta(
                -1, // todo ???????
                TextType.genqs,
                null,
                null
        );
    }
}
