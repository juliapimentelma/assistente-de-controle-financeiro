import { TipoTransacao } from './transacao.model';


export interface SubcategoriaResponse {
  id: number;
  nome: string;
  idCategoria: number;
}

export interface SubcategoriaRequest {
  nome: string;
  idCategoria: number;
}


export interface CategoriaResponse {
  id: number;
  nome: string;
  tipo: TipoTransacao; 
  corHex: string;      
  icone: string;       
  subcategorias: SubcategoriaResponse[]; 
}

export interface CategoriaRequest {
  nome: string;
  tipo: TipoTransacao;
  corHex: string;
  icone: string;
}