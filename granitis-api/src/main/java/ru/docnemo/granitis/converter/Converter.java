package ru.docnemo.granitis.converter;

import ru.docnemo.granitis.core.domain.lexical.TermComponent;
import ru.docnemo.granitis.core.domain.lexical.TermDb;

import java.util.Comparator;

public interface Converter {
    default String buildTerm(TermDb termDb) {
        return String.join(
                " ",
                termDb
                        .getComponents()
                        .stream()
                        .sorted(Comparator.comparing(TermComponent::getPosition))
                        .map(t -> t.getLexeme().getLexeme())
                        .toList()
        );
    }
}
