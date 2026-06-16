
export type TipoTransacao = 'RECEITA' | 'DESPESA';
export type StatusTransacao = 'PENDENTE' | 'FINALIZADA';

export interface TransacaoRequest {
  descricao: string;
  tipo: TipoTransacao;
  categoria: string;
  data: string;
  status: StatusTransacao;
  valor: number;
}

export interface TransacaoResponse {
  id: number;
  descricao: string;
  tipo: TipoTransacao;
  categoria: string;
  data: string; 
  status: StatusTransacao;
  valor: number;
}

export interface FiltroTransacao {
  dataInicial?: string;
  dataFinal?: string;
  tipo?: TipoTransacao;
  categoria?: string;
}