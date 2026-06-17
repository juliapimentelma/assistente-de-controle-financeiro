package br.com.ControleFinanceiro.API.repository;

import br.com.ControleFinanceiro.API.dto.CategoriaSomaDTO;
import br.com.ControleFinanceiro.API.dto.CategoriaSomaProjection;
import br.com.ControleFinanceiro.API.entity.Transacao;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    Page<Transacao> findAllByUsuarioIdAndMesCompetenciaAndAnoCompetencia(
            Long usuarioId, Integer mesCompetencia, Integer anoCompetencia, Pageable pageable);

    @Query("""
        SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t 
        WHERE t.usuario.id = :usuarioId 
        AND t.subcategoria.categoria.id = :categoriaId 
        AND t.mesCompetencia = :mes 
        AND t.anoCompetencia = :ano 
        AND t.subcategoria.categoria.tipo = 'DESPESA'
    """)
    BigDecimal somarGastosPorCategoriaEMes(
            @Param("usuarioId") Long usuarioId,
            @Param("categoriaId") Long categoriaId,
            @Param("mes") Integer mes,
            @Param("ano") Integer ano);

    List<Transacao> findTop5ByUsuarioIdOrderByDataCriacaoDesc(Long usuarioId);

    @Query("SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t WHERE t.usuario.id = :usuarioId AND t.mesCompetencia = :mes AND t.anoCompetencia = :ano AND t.categoria.tipo = 'DESPESA'")
    BigDecimal somarDespesasDoMes(@Param("usuarioId") Long usuarioId, @Param("mes") int mes, @Param("ano") int ano);

    @Query("SELECT c.nome as nome, SUM(t.valor) as total FROM Transacao t JOIN t.categoria c WHERE t.usuario.id = :usuarioId AND t.mesCompetencia = :mes AND t.anoCompetencia = :ano AND c.tipo = 'DESPESA' GROUP BY c.nome")
    List<CategoriaSomaProjection> somarDespesasAgrupadasPorCategoria(@Param("usuarioId") Long usuarioId, @Param("mes") int mes, @Param("ano") int ano);

    @Query("SELECT new br.com.ControleFinanceiro.API.dto.CategoriaSomaDTO(t.categoria.nome, SUM(t.valor)) " +
            "FROM Transacao t " +
            "WHERE t.usuario.id = :usuarioId " +
            "AND t.categoria.tipo = 'DESPESA' " +
            "AND t.mesCompetencia = :mes " +
            "AND t.anoCompetencia = :ano " +
            "GROUP BY t.categoria.nome")
    List<CategoriaSomaDTO> somarDespesasPorCategoria(
            @Param("usuarioId") Long usuarioId,
            @Param("mes") int mes,
            @Param("ano") int ano
    );
}
