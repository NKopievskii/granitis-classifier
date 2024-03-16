package ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.specific;

import ru.docnemo.granitis.semsyn.buildmssr.morph.GrammaticAnalyzer;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.TextType;

public class SpecqsQuant1 extends AbstractSpecificTextTypeDeterminant {
    public SpecqsQuant1(GrammaticAnalyzer grammaticAnalyzer) {
        super(grammaticAnalyzer);
    }

    @Override
    public boolean isThis(String[] text) {
        return text[0].equals("сколько")
                && grammaticAnalyzer.getGrammaticalProperties(text[0]).getPartOfSpeech().equals("сущ")
                && !text[0].equals("раз");
    }

    @Override
    public Meta getMeta(String[] text) {
        return new Meta(
                0, // todo ???????
                TextType.specqsQuant1,
                null,
                null
        );
    }
}
