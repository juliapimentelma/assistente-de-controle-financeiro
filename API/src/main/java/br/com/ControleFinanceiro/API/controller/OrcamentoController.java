package br.com.ControleFinanceiro.API.controller;

import br.com.ControleFinanceiro.API.dto.request.OrcamentoRequest;
import br.com.ControleFinanceiro.API.dto.response.OrcamentoResponse;
import br.com.ControleFinanceiro.API.service.OrcamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orcamentos")
@RequiredArgsConstructor
public class OrcamentoController {

    private final OrcamentoService orcamentoService;

    @GetMapping
    public ResponseEntity<List<OrcamentoResponse>> listarPorCompetencia(
            @RequestParam Integer mes,
            @RequestParam Integer ano) {

        List<OrcamentoResponse> response = orcamentoService.listarPorMesEAno(mes, ano);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<OrcamentoResponse> definirOrcamento(@RequestBody @Valid OrcamentoRequest request) {
        OrcamentoResponse response = orcamentoService.definirOrcamento(request);
        return ResponseEntity.ok(response);
    }
}