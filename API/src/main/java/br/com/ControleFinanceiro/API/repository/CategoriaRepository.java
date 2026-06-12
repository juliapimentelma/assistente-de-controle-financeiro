package br.com.ControleFinanceiro.API.repository;

import br.com.ControleFinanceiro.API.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findAllByUsuarioId(Long usuarioId);
}