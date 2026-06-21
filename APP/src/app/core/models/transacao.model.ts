export type TipoTransacao = 'RECEITA' | 'DESPESA';
export type StatusTransacao = 'PENDENTE' | 'PAGO';
export type FrequenciaParcela = 'SEMANAL' | 'QUINZENAL' | 'MENSAL';

export type CampoOrdenacao = 'dataVencimento' | 'categoria' | 'status' | 'valor';
export type DirecaoOrdenacao = 'asc' | 'desc';

export interface TransacaoRequest {
  id?: number;
  descricao: string;
  valor: number;
  dataVencimento: string;
  status: StatusTransacao;
  categoriaId: number;
  nomeSubcategoria: string;
  qtdParcelas?: number;
  frequencia?: FrequenciaParcela;
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
  parcelaAtual?: number; 
  totalParcelas?: number;
  grupoId?: string;
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