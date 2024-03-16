package ru.docnemo.granitis.core.domain.frame;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.docnemo.granitis.core.domain.lexical.MorphologicalTrait;
import ru.docnemo.granitis.core.domain.lexical.TermDb;
import ru.docnemo.granitis.core.domain.meaning.Meaning;
import ru.docnemo.granitis.core.domain.meaning.SortDb;

@Entity
@Table(name = "preposition_frames")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrepositionFrameDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_frame")
    private Long idFrame;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_preposition_term")
    private TermDb prepositionTermDb;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_noun_sort_1")
    private SortDb nounSort1;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_noun_sort_2")
    private SortDb nounSort2;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_noun_case_trait_2")
    private MorphologicalTrait caseTraitNoun2;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_meaning_frame")
    private Meaning frameMeaning;

    @Column(name = "comment")
    private String comment;
}
