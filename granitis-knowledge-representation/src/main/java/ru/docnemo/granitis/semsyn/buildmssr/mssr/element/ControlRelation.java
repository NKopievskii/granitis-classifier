package ru.docnemo.granitis.semsyn.buildmssr.mssr.element;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ControlRelation {
    private Integer position;
    private String relation;
}
