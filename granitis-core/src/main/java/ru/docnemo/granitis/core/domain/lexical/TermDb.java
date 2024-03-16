package ru.docnemo.granitis.core.domain.lexical;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.docnemo.granitis.core.domain.meaning.Meaning;

import java.util.List;

@Entity
@Table(name = "terms")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TermDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_term")
    private Long idTerm;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_trait_part_of_speech")
    private MorphologicalTrait partOfSpeech;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_trait_subclass")
    private MorphologicalTrait subclass;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "term_components",
            joinColumns = @JoinColumn(name = "id_term"),
            inverseJoinColumns = @JoinColumn(name = "id_lexeme")
    )
    private List<Lexeme> lexemes;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "term_meanings",
            joinColumns = @JoinColumn(name = "id_term"),
            inverseJoinColumns = @JoinColumn(name = "id_meaning")
    )
    private Meaning meaning;

    @Column(name = "comment")
    private String comment;
}
