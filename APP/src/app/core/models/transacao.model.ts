export type TipoTransacao = 'RECEITA' | 'DESPESA';
export type StatusTransacao = 'PENDENTE' | 'PAGO';

// Chaves reais usadas para ordenação na tabela
export type CampoOrdenacao = 'dataVencimento' | 'categoria' | 'status' | 'valor';
export type DirecaoOrdenacao = 'asc' | 'desc';

export interface TransacaoRequest {
  id?: number;
  descricao: string;
  tipo: TipoTransacao;
  subcategoriaId: number; 
  dataVencimento: string; 
  mesCompetencia: number; 
  anoCompetencia: number; 
  status: StatusTransacao;
  valor: number;
}

export interface TransacaoResponse {
  id: number;
  descricao: string;
  tipo: TipoTransacao;
  nomeSubcategoria: string;
  nomeCategoriaMaior: string;
  dataVencimento: string; 
  mesCompetencia: number;
  anoCompetencia: number;
  status: StatusTransacao;
  valor: number;
  parcelas?: any[];
}

export interface FiltroTransacao {
  dataInicial?: string;
  dataFinal?: string;
  tipo?: TipoTransacao;
  categoria?: string;
}

export interface PageResponse<T> {
  content: T[];
  pageable: any;
  last: boolean;
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}