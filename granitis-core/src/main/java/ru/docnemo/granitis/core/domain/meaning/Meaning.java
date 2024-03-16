package ru.docnemo.granitis.core.domain.meaning;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "meanings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meaning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_meaning")
    private Long idMeaning;

    @JoinColumn(name = "meaning")
    private String meaning;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_type")
    private MeaningType type;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "meanings_sorts",
            joinColumns = @JoinColumn(name = "id_meaning"),
            inverseJoinColumns = @JoinColumn(name = "id_sort")
    )
    private List<SortDb> sorts;


    // todo добавить сюда список сортов
}
