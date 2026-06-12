package br.com.ControleFinanceiro.API.entity;

import br.com.ControleFinanceiro.API.entity.emuns.StatusMeta;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_meta_projeto")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MetaProjeto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(name = "valor_alvo")
    private BigDecimal valorAlvo;

    @Column(name = "valor_atual")
    private BigDecimal valorAtual;

    @Enumerated(EnumType.STRING)
    private StatusMeta status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}