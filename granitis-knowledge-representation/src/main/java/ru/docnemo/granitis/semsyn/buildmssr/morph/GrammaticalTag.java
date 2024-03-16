package ru.docnemo.granitis.semsyn.buildmssr.morph;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GrammaticalTag {
    private String baseForm;
    private String partOfSpeech;
    private String subclassPartOfSpeech;
    private String animacy;
    private String gender;
    private String number;
    private String grammaticalCase;
    private String aspect;
    private String transitivity;
    private String person;
    private String tense;
    private String mood;
    private String voice;
    private String reflect;
    private String involvement;



}
