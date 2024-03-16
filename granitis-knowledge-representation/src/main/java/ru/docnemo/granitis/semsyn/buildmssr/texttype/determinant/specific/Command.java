package ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.specific;

import ru.docnemo.granitis.semsyn.buildmssr.morph.GrammaticAnalyzer;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.TextType;

public class Command extends AbstractSpecificTextTypeDeterminant {
    public Command(GrammaticAnalyzer grammaticAnalyzer) {
        super(grammaticAnalyzer);
    }

    @Override
    public boolean isThis(String[] text) {
        return grammaticAnalyzer.getGrammaticalProperties(text[0]).getPartOfSpeech().equals("глагол")
                && (
                grammaticAnalyzer.getGrammaticalProperties(text[0]).getMood().equals("неопр")
                        || grammaticAnalyzer.getGrammaticalProperties(text[0]).getMood().equals("повелит")
        );
    }

    @Override
    public Meta getMeta(String[] text) {
        return new Meta(
                0, // todo ???????
                TextType.imp,
                null,
                null
        );
    }
}
