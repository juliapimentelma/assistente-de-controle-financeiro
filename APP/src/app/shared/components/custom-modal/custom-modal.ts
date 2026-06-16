import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-custom-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './custom-modal.html',
  styleUrl: './custom-modal.css',
})
export class CustomModal {
  @Input({ required: true }) titulo!: string;
  @Input({ required: true }) mensagem!: string;
  @Input() tipo: 'sucesso' | 'erro' = 'sucesso';

  @Output() fechar = new EventEmitter<void>();

  onFechar(): void {
    this.fechar.emit();
  }
}