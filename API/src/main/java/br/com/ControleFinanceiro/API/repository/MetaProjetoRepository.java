package br.com.ControleFinanceiro.API.repository;

import br.com.ControleFinanceiro.API.entity.MetaProjeto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetaProjetoRepository extends JpaRepository<MetaProjeto, Long> {
    List<MetaProjeto> findAllByUsuarioId(Long usuarioId);
}
