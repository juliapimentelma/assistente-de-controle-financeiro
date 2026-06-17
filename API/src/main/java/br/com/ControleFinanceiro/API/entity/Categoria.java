package br.com.ControleFinanceiro.API.entity;

import br.com.ControleFinanceiro.API.entity.emuns.TipoCategoria;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_categoria")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoCategoria tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_pai_id")
    private Categoria categoriaPai;

    @OneToMany(mappedBy = "categoriaPai", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Categoria> subcategorias = new ArrayList<>();
}