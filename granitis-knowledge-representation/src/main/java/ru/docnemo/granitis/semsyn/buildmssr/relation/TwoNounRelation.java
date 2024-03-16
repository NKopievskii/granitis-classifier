package ru.docnemo.granitis.semsyn.buildmssr.relation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwoNounRelation {
    String noun1Meaning; // это должны быть сем значения сущесвительных, глагола и т.п.
    String noun2Meaning;
    String relation;
}
