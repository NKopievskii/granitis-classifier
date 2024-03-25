package ru.docnemo.granitis.converter;

import lombok.extern.slf4j.Slf4j;
import ru.docnemo.granitis.core.domain.lexical.TermDb;
import ru.docnemo.granitis.core.domain.meaning.SortDb;
import ru.docnemo.granitis.semsyn.buildmssr.frame.Term;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class TermConverter implements Converter {
    public Term convert(TermDb termDb) {
        log.debug("TermDb: {}", termDb);
        return new Term(
                termDb.getPartOfSpeech().getTrait(),
                Objects.nonNull(termDb.getSubclass())
                        ? termDb.getSubclass().getTrait()
                        : null,
                Objects.nonNull(termDb.getMeaning())
                        ? termDb.getMeaning().getMeaning()
                        : null, // все менять
                buildTerm(termDb),
                termDb.getComponents().size(),
                Objects.nonNull(termDb.getMeaning())
                        ? termDb.getMeaning()
                        .getSorts()
                        .stream()
                        .flatMap(sortDb -> getSortSpectr(sortDb).stream())
                        .collect(Collectors.toSet())
                        : null
        );
    }

    Set<String> getSortSpectr(SortDb sortDb) {
        Set<String> sorts;
        if (Objects.isNull(sortDb.getHyperonim())) {
            sorts = new HashSet<>();
        } else {
            sorts = getSortSpectr(sortDb.getHyperonim());
        }
        sorts.add(sortDb.getSort());
        return sorts;
    }
}
