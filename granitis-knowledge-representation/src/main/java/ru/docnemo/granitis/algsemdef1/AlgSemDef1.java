package ru.docnemo.granitis.algsemdef1;

import lombok.RequiredArgsConstructor;
import ru.docnemo.granitis.semsyn.SemSyn;
import ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary.LexicalDictionary;
import ru.docnemo.granitis.semsyn.buildmssr.morph.GrammaticAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class AlgSemDef1 {
    private final GrammaticAnalyzer grammaticAnalyzer;
    private final LexicalDictionary lexicalDictionary;
    private final SemSyn semSyn;

    List<Integer> posVerb;
    private String foundLink;
    private List<Boolean> negations;
    private String wordBase;
    private List<String> sem1;
    private String focusSem;
    private String semBase;


    void firstStage(
            String[] defText,
            String domain
    ) {
        posVerb = new ArrayList<>(10);
        negations = new ArrayList<>();
        sem1 = new ArrayList<>();

        findVerbalForms(defText, domain);
        processLogConnectives(defText);
        buildInnerSemImage(defText);
    }

    void buildInnerSemImage(String[] defText) {
        for (int i = 0; i < posVerb.size(); i++) {
            int k = posVerb.get(i);
            String verb = defText[k];
//            String currentPartOfSpeech = grammaticAnalyzer.getGrammaticalProperties(verb).getPartOfSpeech();
//            if (currentPartOfSpeech) {
//
//            }

//            If NOT (current-speech-part = Present Simple Tense)
//            then modif-verb:= Standardform(verb)
// else modif-verb:= verb end-if
//            nt:= length(deftext)

            String modifVerb = grammaticAnalyzer.getGrammaticalProperties(verb).getBaseForm();

            int m;
            if (i == posVerb.size() - 1) {
                m = defText.length - 1;
            } else {
                m = posVerb.get(i + 1) - 1;
            }
            String phrase = wordBase + " " + modifVerb + " " + String.join(" ", Arrays.stream(defText).skip(k + 1).limit(m - k).toList());
            String semRepr1 = semSyn.getSemRepresentation(phrase);

            String semRepr2;
            if (negations.size() > i && negations.get(i)) {
                semRepr2 = "NOT " + semRepr1;
            } else {
                semRepr2 = semRepr1;
            }
            sem1.add(i, semRepr2);
        }
    }

    void processLogConnectives(String[] defText) {
        foundLink = "AND";
        for (int k = 3; k < defText.length; k++) {
            if (defText[k].equals("или")) {
                foundLink = "OR";
            }
        }
        negations = new ArrayList<>();
        for (int k = 1; k < posVerb.size(); k++) {
            negations.add(k, defText[k - 1].equals("не"));
        }
    }

    void findVerbalForms(
            String[] defText,
            String domain
    ) {
//        String focusMap = semMap(lcs(defText[0]), domain);
//        focusSem = lexicalDictionary
//                .findTerm(defText[0]) // Здесь надо бы учитывать domain
//                .getMeaning();
        focusSem = "Труба";

//        String semBase = semMap(lcs(defText[2]), domain);
        semBase = lexicalDictionary
                .findTerm(defText[2])
                .getMeaning();

        wordBase = defText[2];

//        int pvb = p; // найти ближайщую глагольную форму справа
        int pvb = -1;
        for (int i = 2; i < defText.length; i++) {
            if (defText[i].equals(",")) {
                continue;
            }
            if (
                    List.of("глаг", "прич").contains(
                            grammaticAnalyzer
                                    .getGrammaticalProperties(defText[i])
                                    .getPartOfSpeech()
                    )
            ) {
                pvb = i;
                break;
            }
        }
        String vbPartOfSpeech = grammaticAnalyzer.getGrammaticalProperties(defText[pvb]).getPartOfSpeech();

        // скорее всего сюда цикл до конца списка

        posVerb.addFirst(pvb);
        for (int i = pvb + 1; i < defText.length; i++) {
            if (
                    List.of("глаг", "прич").contains(
                            grammaticAnalyzer
                                    .getGrammaticalProperties(defText[i])
                                    .getPartOfSpeech()
                    )
            ) {
                posVerb.add(pvb);
            }
        }
    }

    void renameVariables() {
        for (int i = 1; i < sem1.size(); i++) {
            sem1.set(i, sem1.get(i).replace("e1", "e" + i));
            // еще нужно переименовать x-переменные
        }
    }

    String finalStage() {
        String semDescription = sem1.getFirst();
        boolean isStartCycle = false;
        for (int i = 1; i < posVerb.size(); i++) {
            if (!isStartCycle) {
                isStartCycle = true;
            }
            semDescription = semDescription + foundLink + sem1.get(i);
        }
        if (isStartCycle) {
            semDescription = "(%s)".formatted(semDescription);
        }

        String finalSem = "Определение(%s, %s, x1, %s)".formatted(
                focusSem,
                semBase,
                semDescription
        );

        return finalSem;
    }

    public String getSemRepresentation(String[] text, String domain) {
        if (text[1].equals("-")) {
            text[1] = "есть";
        }

        firstStage(text, domain);
        renameVariables();
        return finalStage();
    }

}
