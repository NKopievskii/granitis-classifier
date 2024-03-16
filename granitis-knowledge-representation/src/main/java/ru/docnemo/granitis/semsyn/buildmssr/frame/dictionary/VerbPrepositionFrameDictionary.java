package ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary;

import ru.docnemo.granitis.semsyn.buildmssr.frame.VerbPrepositionFrame;

import java.util.List;
import java.util.Set;

public interface VerbPrepositionFrameDictionary {
    List<VerbPrepositionFrame> find(
            String situationMeaning,
            String form,
            String reflection,
            String voice,
            String preposition,
            Set<String> sorts,
            Set<String> grammaticalCases
    );
    List<VerbPrepositionFrame> findForConstruct(
            String situationMeaning,
            String prep
    );
}
