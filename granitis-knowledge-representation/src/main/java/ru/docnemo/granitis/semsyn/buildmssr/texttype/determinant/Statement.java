package ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant;

import ru.docnemo.granitis.semsyn.buildmssr.texttype.TextType;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.specific.Meta;

public class Statement implements DefaultTextTypeDeterminant {
    @Override
    public Meta getMeta(String[] text) {
        return new Meta(
                0, // todo ???????
                TextType.stat,
                null,
                null
        );
    }
}
