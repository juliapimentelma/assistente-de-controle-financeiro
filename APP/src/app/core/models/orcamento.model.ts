export interface OrcamentoRequest {
  categoria: string;
  valorLimite: number;
  mes: number;
  ano: number;
}

export interface OrcamentoResponse {
  id: number;
  categoria: string;
  valorLimite: number;
  mes: number;
  ano: number;
  valorJaGasto: number;
  percentualGasto: number;
}