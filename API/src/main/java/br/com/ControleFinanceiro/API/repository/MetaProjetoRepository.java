package br.com.ControleFinanceiro.API.repository;

import br.com.ControleFinanceiro.API.entity.MetaProjeto;
import br.com.ControleFinanceiro.API.entity.emuns.StatusMeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MetaProjetoRepository extends JpaRepository<MetaProjeto, Long> {

    List<MetaProjeto> findAllByUsuarioId(Long usuarioId);

    Optional<MetaProjeto> findFirstByUsuarioIdAndStatus(Long usuarioId, StatusMeta status);
}
