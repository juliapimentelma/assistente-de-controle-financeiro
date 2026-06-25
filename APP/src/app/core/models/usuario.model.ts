export interface UsuarioPerfilResponse {
  id: number;
  nome: string;
  email: string;
  tomIA: 'SARGENTO' | 'CONSELHEIRO';
  scoreFinanceiro: number;
  modoEscuro: boolean;
  tutorialConcluido: boolean;
}

export interface AtualizarPerfilRequest {
  nome?: string;
  tomIA?: 'SARGENTO' | 'CONSELHEIRO';
  modoEscuro?: boolean;
}

export interface AtualizarCredenciaisRequest {
  emailAtual: string;
  senhaAtual: string;
  novoEmail?: string;
  novaSenha?: string;
}