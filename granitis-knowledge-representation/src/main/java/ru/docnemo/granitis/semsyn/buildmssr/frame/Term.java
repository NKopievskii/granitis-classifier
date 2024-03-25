package ru.docnemo.granitis.semsyn.buildmssr.frame;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Term {
    private String partOfSpeech;
    private String subClass;
    private String meaning;
    private String term;
    private int lexemeNumber;
    private Set<String> sorts;
}
