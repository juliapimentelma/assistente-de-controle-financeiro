import { Component, EventEmitter, Input, Output, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './modal.html',
  styleUrl: './modal.css',
})
export class Modal {
  
  @Input({ required: true }) titulo!: string;

  @Input() maxWidth: string = '500px';

  @Output() fechar = new EventEmitter<void>();

  @HostListener('document:keydown.escape')
  onKeydownHandler() {
    this.fecharModal();
  }

  fecharModal() {
    this.fechar.emit();
  }

  prevenirFechamento(event: Event) {
    event.stopPropagation();
  }
}