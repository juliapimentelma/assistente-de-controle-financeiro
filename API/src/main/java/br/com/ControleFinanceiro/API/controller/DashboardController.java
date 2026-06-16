package br.com.ControleFinanceiro.API.controller;

import br.com.ControleFinanceiro.API.dto.response.DashboardResponse;
import br.com.ControleFinanceiro.API.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/resumo")
    public ResponseEntity<DashboardResponse> getResumo(Authentication authentication) {
        // authentication.getName() pega a identidade principal do Token de forma segura
        String identificador = authentication.getName();

        System.out.println("========== DEBUG DASHBOARD ==========");
        System.out.println("Buscando usuário pelo Token: [" + identificador + "]");
        System.out.println("=====================================");

        DashboardResponse response = dashboardService.montarResumo(identificador);

        return ResponseEntity.ok(response);
    }
}