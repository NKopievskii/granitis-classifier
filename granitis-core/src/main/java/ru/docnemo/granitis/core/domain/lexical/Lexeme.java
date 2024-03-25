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
@Table(name = "lexemes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lexeme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lexeme")
    private Long idLexeme;

    @Column(name = "lexeme")
    private String lexeme;
}
