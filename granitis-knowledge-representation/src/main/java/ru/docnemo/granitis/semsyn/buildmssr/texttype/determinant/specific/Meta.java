package ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.specific;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.TextType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meta {
    private int startPosition;
    private TextType textType;
    private String leftPreposition;
    private String var;
}
