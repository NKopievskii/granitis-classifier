package ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.specific;

import ru.docnemo.granitis.semsyn.buildmssr.morph.GrammaticAnalyzer;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.TextType;

public class SpecqsRolWithPrep extends AbstractSpecificTextTypeDeterminant {
    public SpecqsRolWithPrep(GrammaticAnalyzer grammaticAnalyzer) {
        super(grammaticAnalyzer);
    }

    @Override
    public boolean isThis(String[] text) {
        return grammaticAnalyzer.getGrammaticalProperties(text[0]).getPartOfSpeech().equals("предлог")
                && grammaticAnalyzer.getGrammaticalProperties(text[1]).getSubclassPartOfSpeech().equals("вопр-относ-местоим")
                && !grammaticAnalyzer.getGrammaticalProperties(text[1]).getBaseForm().equals("какой");
    }

    @Override
    public Meta getMeta(String[] text) {
        return new Meta(
                0, // todo ???????
                TextType.specqsRol,
                text[0],
                "x1"
        );
    }
}
