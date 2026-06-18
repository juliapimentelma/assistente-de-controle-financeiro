package br.com.ControleFinanceiro.API.controller;

import br.com.ControleFinanceiro.API.dto.request.AporteRequest;
import br.com.ControleFinanceiro.API.dto.request.MetaRequest;
import br.com.ControleFinanceiro.API.dto.response.MetaResponse;
import br.com.ControleFinanceiro.API.service.MetaProjetoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metas")
@RequiredArgsConstructor
public class MetaProjetoController {

    private final MetaProjetoService metaService;

    @GetMapping
    public ResponseEntity<List<MetaResponse>> listar() {
        List<MetaResponse> response = metaService.listarMetas();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<MetaResponse> criar(@RequestBody @Valid MetaRequest request) {
        MetaResponse response = metaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/aporte")
    public ResponseEntity<MetaResponse> realizarAporte(
            @PathVariable Long id,
            @RequestBody @Valid AporteRequest request) {

        MetaResponse response = metaService.realizarAporte(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        metaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}