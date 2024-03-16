package ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary;

import ru.docnemo.granitis.semsyn.buildmssr.frame.Term;

import java.util.List;


public interface LexicalDictionary {
    Term findTerm(String unit);
    List<Term> findTerms(String unit);
}
