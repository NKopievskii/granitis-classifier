package ru.docnemo.granitis.semsyn.buildmssr.mssr.element;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.docnemo.granitis.semsyn.buildmssr.frame.Term;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MssrElement {
    private String unit;
    private List<Term> terms;

    private String prep;
    private List<ControlRelation> controlRelations;

    private String mark;

    private Integer quantity;
    private Integer control;
    private Integer adjectivesNumber;

}
