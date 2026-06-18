import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PossoComprarService } from '../../core/services/posso-comprar';
import { MensagemChat, PossoComprarRequest } from '../../core/models/posso-comprar';

@Component({
  selector: 'app-posso-comprar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './posso-comprar.html',
  styleUrl: './posso-comprar.css',
})
export class PossoComprar {
  
  private readonly chatService = inject(PossoComprarService);

  readonly mensagens = signal<MensagemChat[]>([
    { 
      remetente: 'ia', 
      texto: 'Olá! Sou seu Conselheiro Financeiro (ou o seu Sargento!). O que você quer comprar hoje e quanto custa?' 
    }
  ]);

  item = '';
  valor: number | null = null;
  carregando = signal(false);

  enviarPergunta(): void {
    if (!this.item || !this.valor) return;

    const itemSalvo = this.item;
    const valorSalvo = this.valor;
    const textoUsuario = `Quero comprar ${itemSalvo} por R$ ${valorSalvo}. Posso comprar?`;

    this.mensagens.update(lista => [...lista, { remetente: 'usuario', texto: textoUsuario }]);
    
    this.item = '';
    this.valor = null;
    this.carregando.set(true);

    const request: PossoComprarRequest = {
      item: itemSalvo,
      valor: valorSalvo
    };

    this.chatService.consultarIA(request).subscribe({
      next: (response) => {
        this.mensagens.update(lista => [...lista, { 
          remetente: 'ia', 
          texto: response.resposta 
        }]);
        this.carregando.set(false);
      },
      error: (err) => {
        console.error('Erro ao consultar a IA:', err);
        
        this.mensagens.update(lista => [...lista, { 
          remetente: 'ia', 
          texto: 'Comunicação com o QG interrompida! Tente novamente mais tarde.' 
        }]);
        this.carregando.set(false);
      }
    });
  }
}