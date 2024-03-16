package ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary;

import ru.docnemo.granitis.semsyn.buildmssr.frame.PrepositionFrame;

import java.util.List;
import java.util.Set;

public interface PrepositionFrameDictionary {
    List<PrepositionFrame> find(
            String preposition,
            Set<String> noun1Sorts,
            Set<String> noun2Sorts,
            Set<String> noun2GrCases
    );
}
