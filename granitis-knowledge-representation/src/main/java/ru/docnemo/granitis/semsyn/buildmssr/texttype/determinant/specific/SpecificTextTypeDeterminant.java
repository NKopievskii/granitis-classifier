package ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.specific;

import ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.DefaultTextTypeDeterminant;

public interface SpecificTextTypeDeterminant extends DefaultTextTypeDeterminant {
    boolean isThis(String[] text);
}
