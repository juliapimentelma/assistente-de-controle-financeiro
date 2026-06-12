package br.com.ControleFinanceiro.API.controller;

import br.com.ControleFinanceiro.API.dto.request.SubcategoriaRequest;
import br.com.ControleFinanceiro.API.dto.response.CategoriaResponse;
import br.com.ControleFinanceiro.API.dto.response.SubcategoriaResponse;
import br.com.ControleFinanceiro.API.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listar() {
        List<CategoriaResponse> response = categoriaService.listarCategoriasDoUsuario();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/subcategorias")
    public ResponseEntity<SubcategoriaResponse> criarSubcategoria(@RequestBody @Valid SubcategoriaRequest request) {
        SubcategoriaResponse response = categoriaService.criarSubcategoria(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
