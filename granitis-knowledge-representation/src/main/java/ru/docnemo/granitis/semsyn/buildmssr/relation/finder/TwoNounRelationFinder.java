package ru.docnemo.granitis.semsyn.buildmssr.relation.finder;

import lombok.RequiredArgsConstructor;
import ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary.SortDictionary;
import ru.docnemo.granitis.semsyn.buildmssr.relation.TwoNounRelation;
import ru.docnemo.granitis.semsyn.buildmssr.frame.PrepositionFrame;
import ru.docnemo.granitis.semsyn.buildmssr.frame.Term;
import ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary.LexicalDictionary;
import ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary.PrepositionFrameDictionary;
import ru.docnemo.granitis.semsyn.buildmssr.morph.GrammaticAnalyzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
public class TwoNounRelationFinder {
    private final GrammaticAnalyzer grammaticAnalyzer;
    private final LexicalDictionary lexicalDictionary;
    private final PrepositionFrameDictionary prepositionFrameDictionary;
    private final SortDictionary sortDictionary;

    public List<TwoNounRelation> findRelation(
            String noun1Unit,
            String noun2Unit,
            String preposition2

    ) {

        Set<String> grCases = grammaticAnalyzer.getGrammaticalProperties(noun2Unit).getCases();

        List<TwoNounRelation> relations = new ArrayList<>();
        for (Term term1 : lexicalDictionary.findTerms(noun1Unit)) {
            Set<String> allSorts1 = buildSortsSet(term1);
            for (Term term2 : lexicalDictionary.findTerms(noun2Unit)) {
                Set<String> allSorts2 = buildSortsSet(term2);
                List<PrepositionFrame> frames = prepositionFrameDictionary.find(
                        preposition2,
                        allSorts1,
                        allSorts2,
                        grCases
                );
                relations.addAll(
                        frames
                                .stream()
                                .map(
                                        frame -> new TwoNounRelation(
                                                term1.getMeaning(),
                                                term2.getMeaning(),
                                                frame.getFrameMeaning()
                                        )
                                )
                                .toList()
                );
            }
        }

        return relations;
    }

    Set<String> buildSortsSet(Term term) {
        Set<String> allSorts = new HashSet<>();
        Set<String> sorts1 = term.getSorts();
        if (!sorts1.isEmpty()) {
            sorts1
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(sort -> allSorts.addAll(sortDictionary.sortSpectre(sort)));
        }
        return allSorts;
    }
}
