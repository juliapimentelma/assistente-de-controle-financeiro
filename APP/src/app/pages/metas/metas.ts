import { Component, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameProgress } from '../../shared/game-progress/game-progress';

@Component({
  selector: 'app-metas',
  standalone: true,
  imports: [CommonModule, GameProgress],
  templateUrl: './metas.html',
  styleUrl: './metas.css',
})
export class Metas {
  // Simulando os dados que virão do Backend futuramente
  meta = signal({
    nome: 'Montar meu PC Gamer',
    valorAlvo: 10000,
    valorAtual: 1500
  });

  // Calcula a porcentagem automaticamente sempre que o valorAtual mudar
  progresso = computed(() => {
    const perc = (this.meta().valorAtual / this.meta().valorAlvo) * 100;
    return Math.min(perc, 100); // Garante que não passe de 100%
  });

  // Função para testarmos a animação do Mario no Front
  adicionarFundos(valor: number) {
    this.meta.update(m => ({ 
      ...m, 
      valorAtual: Math.min(m.valorAtual + valor, m.valorAlvo) 
    }));
  }
}