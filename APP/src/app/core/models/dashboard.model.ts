export interface ResumoDashboardResponse {
  saldoAtual: number;
  receitasPendentes: number;
  despesasPendentes: number;
  distribuicaoDespesas: CategoriaAgrupada[];
}

export interface CategoriaAgrupada {
  nomeCategoria: string;
  valorTotal: number;
  percentual: number;
}