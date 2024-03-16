package ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.specific;

import lombok.RequiredArgsConstructor;
import ru.docnemo.granitis.semsyn.buildmssr.morph.GrammaticAnalyzer;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.TextType;

@RequiredArgsConstructor
public class SpecqsRelat1 implements SpecificTextTypeDeterminant {
    private final GrammaticAnalyzer grammaticAnalyzer;
    @Override
    public boolean isThis(String[] text) {
        return grammaticAnalyzer.getGrammaticalProperties(text[0]).getSubclassPartOfSpeech().equals("вопр-относ-местоим")
                && grammaticAnalyzer.getGrammaticalProperties(text[0]).getBaseForm().equals("какой");
    }

    @Override
    public Meta getMeta(String[] text) {
        return new Meta(
                0,
                TextType.specqsRelat1,
                null,
                "x1"
        );
    }
}
