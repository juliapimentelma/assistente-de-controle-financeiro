import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardResponse } from '../../core/models/dashboard.model';
import { DashboardService } from '../../core/services/dashboard';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {
  private dashboardService = inject(DashboardService);

  dados = signal<DashboardResponse | null>(null);
  carregando = signal<boolean>(true);

  ngOnInit(): void {
    this.dashboardService.getResumo().subscribe({
      next: (response) => {
        this.dados.set(response);
        this.carregando.set(false);
      },
      error: (err) => {
        console.error('Erro ao carregar dashboard', err);
        this.carregando.set(false);
      }
    });
  }

  getConicGradient(): string {
    const categorias = this.dados()?.categoriasDespesa;
    if (!categorias || categorias.length === 0) return 'conic-gradient(#333 0% 100%)';

    let gradient = '';
    let acumulado = 0;

    categorias.forEach((cat, index) => {
      const inicio = acumulado;
      acumulado += cat.porcentagem;
      const fim = acumulado;
      
      gradient += `${cat.corHex} ${inicio}% ${fim}%`;
      if (index < categorias.length - 1) gradient += ', ';
    });

    return `conic-gradient(${gradient})`;
  }
}