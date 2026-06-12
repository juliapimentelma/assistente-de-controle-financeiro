package br.com.ControleFinanceiro.API.repository;

import br.com.ControleFinanceiro.API.entity.Subcategoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubcategoriaRepository extends JpaRepository<Subcategoria, Long> {

    List<Subcategoria> findByCategoriaId(Long categoriaId);
}
