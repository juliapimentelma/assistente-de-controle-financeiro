package br.com.ControleFinanceiro.API.controller;

import br.com.ControleFinanceiro.API.dto.request.TransacaoRequest;
import br.com.ControleFinanceiro.API.dto.response.TransacaoResponse;
import br.com.ControleFinanceiro.API.service.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService transacaoService;

    @PostMapping
    public ResponseEntity<List<TransacaoResponse>> criar(@RequestBody @Valid TransacaoRequest request) {
        List<TransacaoResponse> response = transacaoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<TransacaoResponse>> listarPorCompetencia(
            @RequestParam Integer mes,
            @RequestParam Integer ano,
            @PageableDefault(size = 20, sort = "dataVencimento") Pageable pageable) {

        Page<TransacaoResponse> response = transacaoService.listarPorCompetencia(mes, ano, pageable);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/pagar")
    public ResponseEntity<Void> marcarComoPago(@PathVariable Long id) {
        transacaoService.marcarComoPago(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        transacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/grupo/{grupoId}")
    public ResponseEntity<List<TransacaoResponse>> listarPorGrupo(@PathVariable String grupoId) {
        List<TransacaoResponse> response = transacaoService.listarPorGrupo(grupoId);
        return ResponseEntity.ok(response);
    }
}
