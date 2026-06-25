package br.com.ControleFinanceiro.API.entity;

import br.com.ControleFinanceiro.API.entity.emuns.TomIA;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_usuario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    private String senha;

    @Column(name = "saldo_atual")
    private BigDecimal saldoAtual;

    @Column(name = "score_financeiro")
    private Integer scoreFinanceiro;

    @Enumerated(EnumType.STRING)
    @Column(name = "tom_ia")
    private TomIA tomIA;

    @Column(name = "modo_escuro")
    private Boolean modoEscuro;

    @Column(name = "tutorial_concluido")
    private Boolean tutorialConcluido;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Categoria> categorias = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Orcamento> orcamentos = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transacao> transacoes = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MetaProjeto> metas = new ArrayList<>();

    @Column(name = "ativo")
    private Boolean ativo = true;
}