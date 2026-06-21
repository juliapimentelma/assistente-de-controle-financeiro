import { Component, signal, computed, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TipoTransacao, StatusTransacao, TransacaoResponse, DirecaoOrdenacao, CampoOrdenacao, TransacaoRequest } from '../../core/models/transacao.model';
import { TransacaoService } from '../../core/services/transacao';
import { Modal } from '../../shared/modal/modal';
import { ExtratoService } from '../../core/services/extrato';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CategoriaService } from '../../core/services/categoria';
import { CategoriaResponse } from '../../core/models/categoria.model';

@Component({
  selector: 'app-transacoes',
  standalone: true,
  imports: [CommonModule, Modal, ReactiveFormsModule],
  templateUrl: './transacoes.html',
  styleUrl: './transacoes.css',
})
export class Transacoes implements OnInit {

  private readonly extratoService = inject(ExtratoService);
  private readonly transacaoService = inject(TransacaoService);
  private readonly categoriaService = inject(CategoriaService);
  private readonly fb = inject(FormBuilder);

  readonly filtroTipo = signal<TipoTransacao | 'TODOS'>('TODOS');
  readonly filtroStatus = signal<StatusTransacao | 'TODOS'>('TODOS');
  readonly filtroBusca = signal('');
  readonly filtroDataInicio = signal('');
  readonly filtroDataFim = signal('');

  readonly ordenarPor = signal<CampoOrdenacao>('dataVencimento');
  readonly direcao = signal<DirecaoOrdenacao>('desc');

  readonly modalAberto = signal(false);
  readonly transacaoEditando = signal<TransacaoResponse | null>(null);

  private readonly transacoesServidor = signal<TransacaoResponse[]>([]);
  readonly categorias = signal<CategoriaResponse[]>([]);

  ngOnInit(): void {
    this.carregarTransacoesDoMes();
    this.carregarCategorias();
  }

  carregarTransacoesDoMes(): void {
    const dataAtual = new Date();
    const mes = dataAtual.getMonth() + 1; 
    const ano = dataAtual.getFullYear();

    this.transacaoService.listarPorCompetencia(mes, ano).subscribe({
      next: (pagina) => this.transacoesServidor.set(pagina.content),
      error: (err) => console.error('Erro ao buscar transações', err)
    });
  }

  carregarCategorias(): void {
    this.categoriaService.listarTodas().subscribe({
      next: (dados) => this.categorias.set(dados),
      error: (err) => console.error('Erro ao carregar categorias', err)
    });
  }

  readonly transacoesFiltradas = computed(() => {
    let lista = this.transacoesServidor();

    const tipo = this.filtroTipo();
    const status = this.filtroStatus();
    const busca = this.filtroBusca().toLowerCase();
    const dataInicio = this.filtroDataInicio();
    const dataFim = this.filtroDataFim();

    if (tipo !== 'TODOS') lista = lista.filter(t => t.tipo === tipo);
    if (status !== 'TODOS') lista = lista.filter(t => t.status === status);
    
    if (busca) {
      lista = lista.filter(t => {
        const descVal = t.descricao?.toLowerCase() || '';
        const catVal = `${t.nomeCategoriaMaior} ${t.nomeSubcategoria}`.toLowerCase();
        return descVal.includes(busca) || catVal.includes(busca);
      });
    }

    if (dataInicio) lista = lista.filter(t => (t.dataVencimento || '') >= dataInicio);
    if (dataFim) lista = lista.filter(t => (t.dataVencimento || '') <= dataFim);

    const campo = this.ordenarPor();
    const dir = this.direcao() === 'asc' ? 1 : -1;

    return [...lista].sort((a, b) => {
      const chaveA = campo === 'categoria' ? a.nomeCategoriaMaior : a[campo as keyof TransacaoResponse] ?? '';
      const chaveB = campo === 'categoria' ? b.nomeCategoriaMaior : b[campo as keyof TransacaoResponse] ?? '';
      
      if (typeof chaveA === 'number' && typeof chaveB === 'number') return (chaveA - chaveB) * dir;
      return String(chaveA).localeCompare(String(chaveB)) * dir;
    });
  });

  alternarOrdenacao(campo: CampoOrdenacao): void {
    if (this.ordenarPor() === campo) {
      this.direcao.update(d => d === 'asc' ? 'desc' : 'asc');
    } else {
      this.ordenarPor.set(campo);
      this.direcao.set('asc');
    }
  }

  atualizarBusca(evento: Event): void { this.filtroBusca.set((evento.target as HTMLInputElement).value); }
  atualizarDataInicio(evento: Event): void { this.filtroDataInicio.set((evento.target as HTMLInputElement).value); }
  atualizarDataFim(evento: Event): void { this.filtroDataFim.set((evento.target as HTMLInputElement).value); }

  transacaoForm: FormGroup = this.fb.group({
    id: [null],
    descricao: ['', Validators.required],
    tipo: ['DESPESA', Validators.required],
    categoriaId: ['', Validators.required], 
    nomeSubcategoria: [''],
    dataVencimento: ['', Validators.required],
    status: ['PENDENTE', Validators.required],
    valorTexto: ['', Validators.required],
    qtdParcelas: [1, [Validators.required, Validators.min(1)]],
    frequencia: ['MENSAL', Validators.required]
  });

  aplicarMascaraMoeda(evento: Event): void {
    const input = evento.target as HTMLInputElement;
    let valorDigitado = input.value.replace(/\D/g, '');

    if (valorDigitado === '') {
      input.value = '';
      this.transacaoForm.get('valorTexto')?.setValue('');
      return;
    }

    const valorNumerico = parseInt(valorDigitado, 10) / 100;
    const valorFormatado = valorNumerico.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    
    input.value = valorFormatado;
    this.transacaoForm.get('valorTexto')?.setValue(valorFormatado);
  }

  abrirModal(transacao?: TransacaoResponse): void {
    this.transacaoEditando.set(transacao ?? null);
    
    if (transacao) {
      const catEncontrada = this.categorias().find(c => 
        c.nome.toLowerCase().trim() === transacao.nomeCategoriaMaior?.toLowerCase().trim()
      );
      const catId = catEncontrada ? catEncontrada.id : '';

      const valorAjustado = transacao.valor.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });

      this.transacaoForm.patchValue({
        id: transacao.id,
        descricao: transacao.descricao,
        tipo: transacao.tipo,
        categoriaId: catId,
        nomeSubcategoria: transacao.nomeSubcategoria || '',
        dataVencimento: transacao.dataVencimento,
        status: transacao.status,
        valorTexto: valorAjustado,
        qtdParcelas: transacao.totalParcelas || 1,
        frequencia: 'MENSAL'
      });
    } else {
      this.transacaoForm.reset({ tipo: 'DESPESA', status: 'PENDENTE', qtdParcelas: 1, frequencia: 'MENSAL', categoriaId: '' });
    }
    
    this.modalAberto.set(true);
  }

  salvarTransacao(): void {
    if (this.transacaoForm.invalid) return;
    
    const formValues = this.transacaoForm.value;
    const valorConvertido = parseFloat(formValues.valorTexto.replace(/\./g, '').replace(',', '.'));

    const request: TransacaoRequest = {
      id: formValues.id,
      descricao: formValues.descricao,
      valor: valorConvertido,
      dataVencimento: formValues.dataVencimento,
      status: formValues.status,
      categoriaId: Number(formValues.categoriaId),
      nomeSubcategoria: formValues.nomeSubcategoria,
      qtdParcelas: formValues.qtdParcelas,
      frequencia: formValues.frequencia
    };

    this.transacaoService.criar(request).subscribe({
      next: () => {
        this.fecharModal();
        this.carregarTransacoesDoMes(); 
      },
      error: (err) => console.error('Erro ao salvar transação', err)
    });
  }

  fecharModal(): void {
    this.modalAberto.set(false);
    this.transacaoEditando.set(null);
  }

  pedirExclusao(transacao: TransacaoResponse): void {
    if(confirm(`Tem certeza que deseja excluir a transação: ${transacao.descricao}?`)) {
      this.transacaoService.deletar(transacao.id).subscribe({
        next: () => this.carregarTransacoesDoMes(),
        error: (err) => console.error('Erro ao excluir transação', err)
      });
    }
  }

  marcarComoPago(transacao: TransacaoResponse): void {
    this.transacaoService.marcarComoPago(transacao.id).subscribe({
      next: () => this.carregarTransacoesDoMes(),
      error: (err) => console.error('Erro ao marcar como pago', err)
    });
  }

  exportarPdf(): void {
    const dataAtual = new Date();
    const mes = dataAtual.getMonth() + 1; 
    const ano = dataAtual.getFullYear();

    this.extratoService.baixarPdf(mes, ano).subscribe({
      next: (arquivoBlob) => {
        const url = window.URL.createObjectURL(arquivoBlob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `Meu_Extrato_${mes}_${ano}.pdf`; 
        link.click();
        window.URL.revokeObjectURL(url);
      },
      error: (err) => console.error('Erro ao exportar PDF', err)
    });
  }

  readonly linhasExpandidas = signal<Set<number>>(new Set());
  readonly parcelasCarregadas = signal<Record<string, TransacaoResponse[]>>({});

  toggleExpandir(t: TransacaoResponse): void {
    if (!t.grupoId || (t.totalParcelas && t.totalParcelas <= 1)) return;

    const atuais = new Set(this.linhasExpandidas());

    if (atuais.has(t.id)) {
      atuais.delete(t.id);
      this.linhasExpandidas.set(atuais);
      return;
    }

    if (!this.parcelasCarregadas()[t.grupoId]) {
      this.transacaoService.listarPorGrupo(t.grupoId).subscribe({
        next: (parcelas) => {
          this.parcelasCarregadas.update(prev => ({ ...prev, [t.grupoId!]: parcelas }));
          atuais.add(t.id);
          this.linhasExpandidas.set(atuais);
        },
        error: (err) => console.error('Erro ao buscar parcelas do grupo', err)
      });
    } else {
       atuais.add(t.id);
       this.linhasExpandidas.set(atuais);
    }
  }
}