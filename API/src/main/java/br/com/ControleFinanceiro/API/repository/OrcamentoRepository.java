package br.com.ControleFinanceiro.API.repository;

import br.com.ControleFinanceiro.API.entity.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

    Optional<Orcamento> findByUsuarioIdAndCategoriaIdAndMesAndAno(
            Long usuarioId, Long categoriaId, Integer mes, Integer ano);

    List<Orcamento> findByUsuarioIdAndMesAndAno(Long usuarioId, Integer mes, Integer ano);
}
