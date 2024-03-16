package ru.docnemo.granitis.semsyn.buildmssr.relation.finder;

import lombok.RequiredArgsConstructor;
import ru.docnemo.granitis.semsyn.buildmssr.relation.VerbNounRelation;
import ru.docnemo.granitis.semsyn.buildmssr.frame.Term;
import ru.docnemo.granitis.semsyn.buildmssr.frame.VerbPrepositionFrame;
import ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary.LexicalDictionary;
import ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary.VerbPrepositionFrameDictionary;
import ru.docnemo.granitis.semsyn.buildmssr.morph.GrammaticAnalyzer;
import ru.docnemo.granitis.semsyn.buildmssr.morph.GrammaticalProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
public class VerbNounRelationFinder {
    private final GrammaticAnalyzer grammaticAnalyzer;
    private final LexicalDictionary lexicalDictionary;
    private final VerbPrepositionFrameDictionary verbPrepositionFrameDictionary;

    public List<VerbNounRelation> findRelation(
            String verbUnit,
            Integer verbControl,
            String dependentWordUnit,
            Integer dependentWordPosition,
            String dependentWordControlUnit,
            String preposition
    ) {
        String prep = lexicalDictionary.findTerm(verbUnit).getPartOfSpeech().equals("прич")
                && Objects.equals(verbControl, dependentWordPosition)
                ? null
                : preposition;

        String dependentWord = Objects.requireNonNullElse(lexicalDictionary.findTerm(dependentWordUnit).getSubClass(), "").equals("вопрос-относ-местоим")
                && List.of("который", "какой").contains(dependentWordUnit)
                ? dependentWordControlUnit
                : dependentWordUnit;

        Set<String> grCases = grammaticAnalyzer.getGrammaticalProperties(dependentWord).getCases();

        if (lexicalDictionary.findTerm(verbUnit).getPartOfSpeech().equals("прич")) {
            if (Objects.equals(verbControl, dependentWordPosition)) {
                grCases.clear();
                grCases.add("им");
            }
        }

        List<VerbNounRelation> relations = new ArrayList<>();

        for (Term depend : lexicalDictionary.findTerms(dependentWordUnit)) {
            Set<String> sorts = depend.getSorts(); // надо доработать, чтобы все дерево возвращали

            // todo может же быть несколько наборов
            GrammaticalProperties verbProperties = grammaticAnalyzer.getGrammaticalProperties(verbUnit);
            for (Term verb : lexicalDictionary.findTerms(verbUnit)) {
                List<VerbPrepositionFrame> frames = verbPrepositionFrameDictionary.find(
                        verb.getMeaning(),
                        verbProperties.getMood(),
                        verbProperties.getReflect(),
                        verbProperties.getVoice(),
                        prep,
                        sorts,
                        grCases
                );
                relations.addAll(
                        frames
                                .stream()
                                .map(
                                        frame -> new VerbNounRelation(
                                                verb.getMeaning(),
                                                depend.getMeaning(),
                                                frame.getCaseTrait(),
                                                frame.getFrameMeaning()
                                        )
                                )
                                .toList()
                );
            }
        }

        return relations;
    }

    public List<VerbNounRelation> findRelationForConstruct(
            String verbUnit,
            String preposition
    ) {

        List<VerbNounRelation> relations = new ArrayList<>();
        for (Term verb : lexicalDictionary.findTerms(verbUnit)) {
            List<VerbPrepositionFrame> frames = verbPrepositionFrameDictionary.findForConstruct(
                    verb.getMeaning(),
                    preposition
            );
            relations.addAll(
                    frames
                            .stream()
                            .map(
                                    frame -> new VerbNounRelation(
                                            verb.getMeaning(),
                                            null,
                                            frame.getCaseTrait(),
                                            frame.getFrameMeaning()
                                    )
                            )
                            .toList()
            );
        }
        return relations;
    }
}
