import { Component, inject } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CustomInput } from '../../../shared/components/custom-input/custom-input';
import { AuthService } from '../../../core/services/auth';
import { HttpErrorResponse } from '@angular/common/http';
import { CustomModal } from '../../../shared/components/custom-modal/custom-modal';

@Component({
  selector: 'app-cadastro',
  standalone: true,
  imports: [ReactiveFormsModule, CustomInput, RouterLink, CustomModal],
  templateUrl: './cadastro.html',
  styleUrl: './cadastro.css',
})
export class Cadastro {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private authService = inject(AuthService);

  exibirModal = false;
  tituloModal = '';
  mensagemModal = '';
  tipoModal: 'sucesso' | 'erro' = 'sucesso';

  cadastroForm = this.fb.group({
    nome: ['', Validators.required],
    email: ['', [
      Validators.required, 
      Validators.pattern(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/)
    ]],
    senha: ['', [
      Validators.required, 
      Validators.minLength(6),
      Validators.pattern(/(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])/) 
    ]],
    confirmarSenha: ['', Validators.required],
    tomIA: ['CONSELHEIRO', Validators.required]
  }, { 
    validators: this.validarSenhasIguais 
  });

  validarSenhasIguais(group: AbstractControl): ValidationErrors | null {
    const senha = group.get('senha')?.value;
    const confirmar = group.get('confirmarSenha')?.value;
    if (senha && confirmar && senha !== confirmar) {
      return { senhasDiferentes: true };
    }
    return null;
  }

  onSubmit(): void {
    if (this.cadastroForm.invalid) return;

    const request = {
      nome: this.cadastroForm.value.nome!,
      email: this.cadastroForm.value.email!,
      senha: this.cadastroForm.value.senha!,
      tomIA: this.cadastroForm.value.tomIA! as 'SARGENTO' | 'CONSELHEIRO'
    };

    this.authService.cadastrar(request).subscribe({
      next: () => {
        this.tituloModal = 'BEM-VINDO!';
        this.mensagemModal = 'Cadastro realizado com sucesso. Faça login para acessar o copiloto.';
        this.tipoModal = 'sucesso';
        this.exibirModal = true;
      },
      error: (err: HttpErrorResponse) => {
        this.tituloModal = 'ERRO';
        this.mensagemModal = 'Não foi possível realizar o cadastro. Tente outro e-mail.';
        this.tipoModal = 'erro';
        this.exibirModal = true;
      }
    });
  }

  fecharModal(): void {
    this.exibirModal = false;
    if (this.tipoModal === 'sucesso') {
      this.router.navigate(['/login']);
    }
  }
}