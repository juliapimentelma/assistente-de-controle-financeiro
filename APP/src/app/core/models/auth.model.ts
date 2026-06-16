export interface LoginRequest {
  email: string;
  senha: string;
}

export interface LoginResponse {
  token: string;
  idUsuario: number;
  nome: string;
  tomIA: 'SARGENTO' | 'CONSELHEIRO';
  tutorialConcluido: boolean;
  modoEscuro: boolean; 
}

export interface CadastroRequest {
  nome: string;
  email: string;
  senha: string;
  tomIA: 'SARGENTO' | 'CONSELHEIRO'; 
}

export interface CadastroResponse {
  idUsuario: number;
  mensagem: string;
}