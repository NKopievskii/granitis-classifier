package ru.docnemo.granitis.semsyn.buildmssr.morph;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GrammaticalProperties {
    private final List<GrammaticalTag> tag;  // переделать на работу со списком или определние нужного

    public String getPartOfSpeech() {
        return tag.getFirst().getPartOfSpeech(); //todo добавить сюда вывод типа "ПУнктуация"
    }

    public String getSubclassPartOfSpeech() {
        return tag.getFirst().getSubclassPartOfSpeech();
    }

    public String getBaseForm() {
        return tag.getFirst().getBaseForm();
    }
    public List<String> getAllBaseForm() {
        return tag.stream().map(GrammaticalTag::getBaseForm).toList();
    }

    public String getAnimacy() {
        return tag.getFirst().getAnimacy();
    }

    public String getGender() {
        return tag.getFirst().getGender();
    }

    public String getNumber() {
        return tag.getFirst().getNumber();
    }

    public String getCase() {
        return tag.getFirst().getGrammaticalCase();
    }

    public Set<String> getCases() {
        return tag
                .stream()
                .map(
                        GrammaticalTag::getGrammaticalCase
                )
                .collect(Collectors.toSet());
    }

    public String getAspect() {
        return tag.getFirst().getAspect();
    }

    public String getTransitivity() {
        return tag.getFirst().getTransitivity();
    }

    public String getPerson() {
        return tag.getFirst().getPerson();
    }

    public String getTense() {
        return tag.getFirst().getTense();
    }

    public String getMood() {
        return Objects.requireNonNullElse(tag.getFirst().getMood(), "неопр");
    }

    public String getVoice() {
        return tag.getFirst().getVoice();
    }

    public String getReflect() {
        return tag.getFirst().getReflect();
    }

    public String getInvolvement() {
        return tag.getFirst().getInvolvement();
    }


}
