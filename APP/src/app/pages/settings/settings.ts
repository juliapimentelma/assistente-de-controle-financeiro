import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { UsuarioService } from '../../core/services/usuario'; 
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './settings.html',
  styleUrl: './settings.css',
})

export class Settings implements OnInit {
  private fb = inject(FormBuilder);
  private usuarioService = inject(UsuarioService);
  private authService = inject(AuthService);
  private router = inject(Router);

  mensagemAviso = signal<{ texto: string, tipo: 'sucesso' | 'erro' } | null>(null);

  perfilForm = this.fb.group({
    nome: ['', Validators.required],
    email: [{ value: '', disabled: true }], 
    tomIA: ['CONSELHEIRO', Validators.required]
  });

  senhaForm = this.fb.group({
    senhaAtual: ['', Validators.required],
    novaSenha: ['', [Validators.required, Validators.minLength(6)]],
    confirmarSenha: ['', Validators.required]
  }, { validators: this.validarSenhas });

  ngOnInit(): void {
    this.carregarPerfil();
  }

  carregarPerfil(): void {
    this.usuarioService.obterMeuPerfil().subscribe({
      next: (perfil) => {
        this.perfilForm.patchValue({
          nome: perfil.nome,
          email: perfil.email,
          tomIA: perfil.tomIA
        });
      },
      error: () => this.mostrarAviso('Erro ao carregar dados do perfil.', 'erro')
    });
  }

  validarSenhas(group: AbstractControl): ValidationErrors | null {
    const senha = group.get('novaSenha')?.value;
    const confirmar = group.get('confirmarSenha')?.value;
    return senha === confirmar ? null : { senhasDiferentes: true };
  }

  salvarPerfil(): void {
    if (this.perfilForm.invalid) return;

    const dados = {
      nome: this.perfilForm.value.nome!,
      tomIA: this.perfilForm.value.tomIA! as 'CONSELHEIRO' | 'SARGENTO',
      modoEscuro: true 
    };

    this.usuarioService.atualizarPerfil(dados).subscribe({
      next: () => this.mostrarAviso('Perfil atualizado com sucesso!', 'sucesso'),
      error: () => this.mostrarAviso('Erro ao atualizar perfil.', 'erro')
    });
  }

  salvarSenha(): void {
    if (this.senhaForm.invalid) return;

    const emailDoUsuario = this.perfilForm.getRawValue().email;

    const dados = {
      emailAtual: emailDoUsuario,
      senhaAtual: this.senhaForm.value.senhaAtual!,
      novaSenha: this.senhaForm.value.novaSenha!
    };

    this.usuarioService.atualizarCredenciais(dados as any).subscribe({
      next: () => {
        this.mostrarAviso('Credenciais alteradas com sucesso!', 'sucesso');
        this.senhaForm.reset();
      },
      error: () => this.mostrarAviso('Dados incorretos ou erro na alteração.', 'erro')
    });
  }

  desativarConta(): void {
    if (confirm('⚠️ ZONA DE PERIGO: Tem certeza que deseja desativar sua conta? Você perderá o acesso ao sistema.')) {
      this.usuarioService.desativarMinhaConta().subscribe({
        next: () => {
          alert('Conta desativada. Encerrando sessão...');
          this.authService.logout();
          this.router.navigate(['/login']);
        },
        error: () => this.mostrarAviso('Não foi possível desativar a conta.', 'erro')
      });
    }
  }

  mostrarAviso(texto: string, tipo: 'sucesso' | 'erro'): void {
    this.mensagemAviso.set({ texto, tipo });
    setTimeout(() => this.mensagemAviso.set(null), 5000);
  }
}