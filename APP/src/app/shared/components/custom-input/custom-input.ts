import { Component, Input } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-custom-input',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './custom-input.html',
  styleUrl: './custom-input.css',
})
export class CustomInput {
  @Input({ required: true }) numero!: string;
  @Input({ required: true }) label!: string;
  @Input() tipo: string = 'text';
  @Input() placeholder: string = '';
  @Input({ required: true }) control!: FormControl; 

  mostrarSenha = false;

  get inputType(): string {
    if (this.tipo === 'password') {
      return this.mostrarSenha ? 'text' : 'password';
    }
    return this.tipo;
  }

  toggleSenha(event: Event): void {
    event.preventDefault();
    this.mostrarSenha = !this.mostrarSenha;
  }
}