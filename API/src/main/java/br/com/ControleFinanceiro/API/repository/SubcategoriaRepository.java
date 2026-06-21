package br.com.ControleFinanceiro.API.repository;

import br.com.ControleFinanceiro.API.entity.Subcategoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubcategoriaRepository extends JpaRepository<Subcategoria, Long> {

    List<Subcategoria> findByCategoriaId(Long categoriaId);

    Optional<Subcategoria> findByNomeIgnoreCaseAndCategoriaId(String nome, Long categoriaId);
}
