package br.com.souza.spring_boot_essentials.database.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "treinos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TreinosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private AlunosEntity aluno;

    @ManyToMany
    @JoinTable(
            name = "treinos_exercicios",
            joinColumns = @JoinColumn(name = "treino_id"),
            inverseJoinColumns = @JoinColumn(name = "exercicio_id")
    )
    private Set<ExerciciosEntity> exercicios = new HashSet<>();
}
