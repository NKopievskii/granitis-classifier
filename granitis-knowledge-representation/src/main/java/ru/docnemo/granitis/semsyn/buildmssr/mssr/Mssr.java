package ru.docnemo.granitis.semsyn.buildmssr.mssr;

import lombok.Builder;
import lombok.Data;
import ru.docnemo.granitis.semsyn.buildmssr.mssr.element.MssrElement;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.TextType;

import java.util.List;

@Data
@Builder
public class Mssr {
    private List<MssrElement> matrix;

    private TextType textType;
    private Integer mainPos;
    private Integer questionWordsNumber;

    public MssrElement get(int i) {
        return matrix.get(i);
    }

    public int size() {
        return matrix.size();
    }
}
