package ru.docnemo.granitis.semsyn;

import lombok.RequiredArgsConstructor;
import ru.docnemo.granitis.semsyn.buildmssr.mssr.builder.MssrBuilder;
import ru.docnemo.granitis.semsyn.sembuild.SemBuilder;

@RequiredArgsConstructor
public class SemSyn {
    private final MssrBuilder mssrBuilder;
    private final SemBuilder semBuilder;

    public String getSemRepresentation(String text) {
        return semBuilder.buildSem(
                mssrBuilder.buildMssr(text.split(" "))
        );
    }
}
