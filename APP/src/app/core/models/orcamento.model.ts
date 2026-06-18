export interface OrcamentoRequest {
  categoriaId: number;
  valorPlanejado: number;
  mes: number;
  ano: number;
}

export interface OrcamentoResponse {
  id: number;
  mes: number;
  ano: number;
  valorPlanejado: number;
  categoriaId: number;
  nomeCategoria: string;
  tipoCategoria: string;
  valorGasto?: number;
  percentual?: number;
}