package br.com.ControleFinanceiro.API.repository;

import br.com.ControleFinanceiro.API.entity.Transacao;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

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
}
