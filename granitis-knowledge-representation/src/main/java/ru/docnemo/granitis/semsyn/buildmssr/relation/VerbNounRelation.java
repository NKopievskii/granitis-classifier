package ru.docnemo.granitis.semsyn.buildmssr.relation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerbNounRelation {
    String verbMeaning;
    String nounMeaning;
    String grammaticalCase;
    String role;
}
