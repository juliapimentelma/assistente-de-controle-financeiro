package br.com.ControleFinanceiro.API.controller;

import br.com.ControleFinanceiro.API.service.ExtratoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/extratos")
@RequiredArgsConstructor
public class ExtratoController {

    private final ExtratoService extratoService;

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> baixarPdf(
            @RequestParam Integer mes,
            @RequestParam Integer ano) {

        Long usuarioIdLogado = 1L;

        byte[] pdfBytes = extratoService.gerarExtratoPdf(usuarioIdLogado, mes, ano);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "extrato_" + mes + "_" + ano + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}