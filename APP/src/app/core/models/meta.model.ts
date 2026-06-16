export interface MetaRequest {
  titulo: string;
  valorObjetivo: number;
  dataAlvo: string;
}

export interface MetaResponse {
  id: number;
  titulo: string;
  valorObjetivo: number;
  valorAtual: number;
  percentualConcluido: number;
  dataAlvo: string;
  status: 'EM_ANDAMENTO' | 'CONCLUIDA';
}

export interface AporteRequest {
  valor: number;
}