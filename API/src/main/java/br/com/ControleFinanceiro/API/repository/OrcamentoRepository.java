package br.com.ControleFinanceiro.API.repository;

import br.com.ControleFinanceiro.API.entity.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

    Optional<Orcamento> findByUsuarioIdAndCategoriaIdAndMesAndAno(
            Long usuarioId, Long categoriaId, Integer mes, Integer ano);

    List<Orcamento> findByUsuarioIdAndMesAndAno(Long usuarioId, Integer mes, Integer ano);

    @Query("SELECT COALESCE(SUM(o.valorPlanejado), 0) FROM Orcamento o WHERE o.usuario.id = :usuarioId AND o.mes = :mes AND o.ano = :ano")
    BigDecimal somarOrcamentoDoMes(@Param("usuarioId") Long usuarioId, @Param("mes") int mes, @Param("ano") int ano);
}
