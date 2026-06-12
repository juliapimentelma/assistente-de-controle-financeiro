package br.com.ControleFinanceiro.API.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_orcamento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Orcamento {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer mes;
    private Integer ano;

    @Column(name = "valor_planejado")
    private BigDecimal valorPlanejado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}