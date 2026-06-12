package br.com.ControleFinanceiro.API.controller;

import br.com.ControleFinanceiro.API.dto.request.PossoComprarRequest;
import br.com.ControleFinanceiro.API.dto.response.IaResponse;
import br.com.ControleFinanceiro.API.dto.response.MultiversoResponse;
import br.com.ControleFinanceiro.API.service.ConselheiroIaService;
import br.com.ControleFinanceiro.API.service.MultiversoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/conselheiro")
@RequiredArgsConstructor
public class ConselheiroController {

    private final ConselheiroIaService conselheiroIaService;
    private final MultiversoService multiversoService;

    @PostMapping("/posso-comprar")
    public ResponseEntity<IaResponse> analisarCompra(@RequestBody @Valid PossoComprarRequest request) {
        IaResponse response = conselheiroIaService.analisarCompra(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/multiverso")
    public ResponseEntity<MultiversoResponse> simularCorteGasto(@RequestParam BigDecimal valorMensalCortado) {
        MultiversoResponse response = multiversoService.simular(valorMensalCortado);
        return ResponseEntity.ok(response);
    }
}