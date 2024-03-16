package ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.specific;

import ru.docnemo.granitis.semsyn.buildmssr.morph.GrammaticAnalyzer;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.TextType;

import java.util.List;

public class SpecqsRolWithoutPrep extends AbstractSpecificTextTypeDeterminant {
    public SpecqsRolWithoutPrep(GrammaticAnalyzer grammaticAnalyzer) {
        super(grammaticAnalyzer);
    }

    @Override
    public boolean isThis(String[] text) {
        return (
                grammaticAnalyzer.getGrammaticalProperties(text[0]).getSubclassPartOfSpeech().equals("вопр-относ-местоим")
                && !grammaticAnalyzer.getGrammaticalProperties(text[0]).getBaseForm().equals("какой")
        )
                || (
                grammaticAnalyzer.getGrammaticalProperties(text[0]).getSubclassPartOfSpeech().equals("местоим-наречие")
                        && List.of("когда", "куда", "откуда", "где").contains(text[0])
        );
    }

    @Override
    public Meta getMeta(String[] text) {
        return new Meta(
                -1, // todo ???????
                TextType.specqsRol,
                null,
                "x1"
        );
    }
}
