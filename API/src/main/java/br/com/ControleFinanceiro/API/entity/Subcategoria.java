package br.com.ControleFinanceiro.API.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_subcategoria")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Subcategoria {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
}