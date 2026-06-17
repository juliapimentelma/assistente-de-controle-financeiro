import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-game-progress',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './game-progress.html',
  styleUrl: './game-progress.css',
})
export class GameProgress {
  @Input() progresso: number = 0; 

  itensTrilha = [
    { posicao: 10, img: 'coin.gif' },
    { posicao: 18, img: 'coin.gif' },
    { posicao: 25, img: 'egg.gif' }, // Pega o Yoshi
    { posicao: 35, img: 'coin.gif' },
    { posicao: 42, img: 'coin.gif' },
    { posicao: 50, img: 'estrela.gif' }, // Voador/Estrela (ajuste o nome do seu gif)
    { posicao: 60, img: 'coin.gif' },
    { posicao: 68, img: 'coin.gif' },
    { posicao: 75, img: 'fire-flower-super-mario-world.gif' }, // Flor
    { posicao: 85, img: 'coin.gif' },
    { posicao: 92, img: 'coin.gif' },
    { posicao: 100, img: 'flag.gif' } // Fim da jornada
  ];

  get marioSprite(): string {
    if (this.progresso < 25) {
      return 'mario01.gif'; // Mario normal
    } else if (this.progresso < 50) {
      return 'mario02.gif';
    } else if (this.progresso < 75) {
      return 'mario03.gif'; 
    } else {
      return 'mario03.gif'; 
    }
  }
}