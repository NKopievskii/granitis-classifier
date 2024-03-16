package ru.docnemo.granitis.core.domain.lexical;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "morphological_trait_types")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MorphologicalTraitType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trait_type")
    private Long idTraitType;

    @Column(name = "trait_type")
    private String traitType;
}
