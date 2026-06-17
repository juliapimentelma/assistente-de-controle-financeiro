export interface TransacaoResumo {
  descricao: string;
  tipo: 'RECEITA' | 'DESPESA' | 'INVESTIMENTO';
  categoria: string;
  data: string;
  status: string;
  valor: number;
}

export interface CategoriaResumo {
  nome: string;
  corHex: string;
  porcentagem: number;
}

export interface DashboardResponse {
  saldoAtual: number;
  scoreFinanceiro: number;
  progressoMeta: number;
  valorDisponivelMes: number;
  categoriasDespesa: CategoriaResumo[];
  ultimasTransacoes: TransacaoResumo[];
}

export interface CategoriaDespesa {
  nome: string;
  porcentagem: number;
  corHex?: string;
}
