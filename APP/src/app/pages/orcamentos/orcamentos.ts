import { Component, OnInit, signal, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { OrcamentoService } from '../../core/services/orcamento';
import { CategoriaService } from '../../core/services/categoria';
import { CategoriaResponse } from '../../core/models/categoria.model';
import { OrcamentoRequest, OrcamentoResponse } from '../../core/models/orcamento.model';

@Component({
  selector: 'app-orcamentos',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './orcamentos.html',
  styleUrl: './orcamentos.css',
})
export class Orcamentos implements OnInit {

  private readonly fb = inject(FormBuilder);
  private readonly orcamentoService = inject(OrcamentoService);
  private readonly categoriaService = inject(CategoriaService);

  readonly categorias = signal<CategoriaResponse[]>([]);
  readonly orcamentosServidor = signal<OrcamentoResponse[]>([]);

  orcamentoForm: FormGroup = this.fb.group({
    mes: [new Date().getMonth() + 1, Validators.required],
    ano: [new Date().getFullYear(), Validators.required],
    categoriaId: ['', Validators.required],
    subcategoriaId: [''], 
    valorTexto: ['', Validators.required]
  });

  ngOnInit(): void {
    this.carregarCategorias();
    this.carregarOrcamentos();
    this.orcamentoForm.get('mes')?.valueChanges.subscribe(() => this.carregarOrcamentos());
    this.orcamentoForm.get('ano')?.valueChanges.subscribe(() => this.carregarOrcamentos());
  }

  carregarCategorias(): void {
    this.categoriaService.listarTodas().subscribe({ 
      next: (dados) => {
        const apenasGastos = dados.filter(cat => cat.tipo !== 'RECEITA');
        this.categorias.set(apenasGastos);
      },
      error: (err) => console.error('Erro ao buscar categorias', err)
    });
  }

  carregarOrcamentos(): void {
    const mes = this.orcamentoForm.get('mes')?.value;
    const ano = this.orcamentoForm.get('ano')?.value;

    if (!mes || !ano) return;

    this.orcamentoService.listarPorCompetencia(mes, ano).subscribe({
      next: (dados) => this.orcamentosServidor.set(dados),
      error: (err) => console.error('Erro ao buscar orçamentos', err)
    });
  }

  readonly orcamentosComProgresso = computed(() => {
    return this.orcamentosServidor()
      .filter(orc => orc.tipoCategoria !== 'RECEITA') 
      .map(orc => {
        const planejado = orc.valorPlanejado > 0 ? orc.valorPlanejado : 1;
        const gasto = orc.valorGasto || 0; 
        const percentual = (gasto / planejado) * 100;
        
        return { ...orc, percentual };
      });
  });

  getCorBarra(percentual: number): string {
    if (percentual < 50) return 'cor-verde';
    if (percentual < 90) return 'cor-amarelo';
    return 'cor-vermelho';
  }

  aplicarMascaraMoeda(evento: Event): void {
    const input = evento.target as HTMLInputElement;
    let valorDigitado = input.value.replace(/\D/g, '');
    
    if (valorDigitado === '') { 
      input.value = ''; 
      return; 
    }
    
    const valorNumerico = parseInt(valorDigitado, 10) / 100;
    input.value = valorNumerico.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
  }

  salvarOrcamento(): void {
    if (this.orcamentoForm.invalid) return;

    const formValues = this.orcamentoForm.value;

    const valorConvertido = parseFloat(formValues.valorTexto.replace(/\./g, '').replace(',', '.'));

    const request: OrcamentoRequest = {
      categoriaId: Number(formValues.categoriaId),
      valorPlanejado: valorConvertido,
      mes: Number(formValues.mes),
      ano: Number(formValues.ano)
    };

    this.orcamentoService.definirOrcamento(request).subscribe({
      next: () => {
        this.carregarOrcamentos();
        this.orcamentoForm.get('valorTexto')?.reset();
        this.orcamentoForm.get('categoriaId')?.setValue('');
        this.orcamentoForm.get('subcategoriaId')?.setValue('');
      },
      error: (err) => {
        console.error('Erro ao salvar orçamento', err);
        alert('Ops! Ocorreu um erro ao definir o orçamento.');
      }
    });
  }

  cancelar(): void {
    this.orcamentoForm.patchValue({
      categoriaId: '',
      valorTexto: ''
    });
  }
}