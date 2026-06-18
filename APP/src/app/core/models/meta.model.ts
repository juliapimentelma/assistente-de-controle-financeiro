export interface MetaRequest {
  titulo: string;
  valorAlvo: number;
  dataAlvo: string;
}

export interface MetaResponse {
  id: number;
  titulo: string;
  valorAlvo: number;
  valorAtual: number;
  percentualConcluido: number;
  dataAlvo: string;
  status: 'EM_ANDAMENTO' | 'CONCLUIDA';
}

export interface AporteRequest {
  valor: number;
}