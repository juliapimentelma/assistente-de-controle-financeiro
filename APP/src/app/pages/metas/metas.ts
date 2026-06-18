import { Component, OnInit, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameProgress } from '../../shared/game-progress/game-progress';
import { MetaRequest, MetaResponse } from '../../core/models/meta.model';
import { MetaService } from '../../core/services/meta';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-metas',
  standalone: true,
  imports: [CommonModule, GameProgress, ReactiveFormsModule],
  templateUrl: './metas.html',
  styleUrl: './metas.css',
})

export class Metas implements OnInit {

  private readonly metaService = inject(MetaService);
  private readonly fb = inject(FormBuilder);

  readonly metas = signal<MetaResponse[]>([]);
  readonly modalAporte = signal({ aberto: false, metaId: 0, valor: 0 });

  readonly modalCriarAberto = signal(false); 

  metaForm: FormGroup = this.fb.group({
    titulo: ['', Validators.required],
    valorAlvo: ['', [Validators.required, Validators.min(1)]],
    dataAlvo: [''] 
  });

  ngOnInit(): void {
    this.carregarMetas();
  }

  carregarMetas(): void {
    this.metaService.listarTodas().subscribe({
      next: (dados) => this.metas.set(dados),
      error: (err) => console.error('Erro ao carregar metas', err)
    });
  }

  abrirModalCriar(): void {
    this.metaForm.reset();
    this.modalCriarAberto.set(true);
  }

  fecharModalCriar(): void {
    this.modalCriarAberto.set(false);
  }

  salvarNovaMeta(): void {
    if (this.metaForm.invalid) return;
    const formValues = this.metaForm.value;
    const valorString = formValues.valorAlvo.replace(/\D/g, '');
    const valorNumerico = Number(valorString) / 100;
    const request: MetaRequest = {
      titulo: formValues.titulo,
      valorAlvo: valorNumerico,
      dataAlvo: formValues.dataAlvo
    };

    this.metaService.criarMeta(request).subscribe({
      next: (novaMeta) => {
        this.metas.update(lista => [...lista, novaMeta]); 
        this.fecharModalCriar();
      },
      error: (err) => {
        console.error('Erro ao criar meta', err);
        alert('Ops! Não foi possível criar a meta. Tente novamente.');
      }
    });
  }

  formatarMoeda(event: Event): void {
    const input = event.target as HTMLInputElement;
    let valor = input.value.replace(/\D/g, '');

    if (!valor) {
      this.metaForm.get('valorAlvo')?.setValue('', { emitEvent: false });
      return;
    }
    const valorNumerico = parseFloat(valor) / 100;
    const valorFormatado = new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(valorNumerico);
    this.metaForm.get('valorAlvo')?.setValue(valorFormatado, { emitEvent: false });
  }

  prepararAporte(metaId: number, valor: number): void {
    this.modalAporte.set({ aberto: true, metaId, valor });
  }

  fecharModal(): void {
    this.modalAporte.set({ aberto: false, metaId: 0, valor: 0 });
  }

  confirmarAporte(): void {
    const { metaId, valor } = this.modalAporte();

    this.metaService.adicionarAporte(metaId, { valor }).subscribe({
      next: (metaAtualizada) => {
        this.metas.update(listaAtual => 
          listaAtual.map(m => m.id === metaId ? metaAtualizada : m)
        );
        this.fecharModal();
      },
      error: (err) => {
        console.error('Erro ao adicionar fundos', err);
        alert('Ops! Não foi possível adicionar o aporte. Verifique seu saldo.');
        this.fecharModal();
      }
    });
  }
}