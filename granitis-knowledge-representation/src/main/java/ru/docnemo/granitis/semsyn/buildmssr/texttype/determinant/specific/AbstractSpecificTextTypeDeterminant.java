package ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.specific;

import lombok.RequiredArgsConstructor;
import ru.docnemo.granitis.semsyn.buildmssr.morph.GrammaticAnalyzer;

@RequiredArgsConstructor
public abstract class AbstractSpecificTextTypeDeterminant implements SpecificTextTypeDeterminant {
    protected final GrammaticAnalyzer grammaticAnalyzer;
}
