export interface PossoComprarRequest {
  item: string;
  valor: number;
}

export interface IaResponse {
  resposta: string;
}

export interface MensagemChat {
  remetente: 'usuario' | 'ia';
  texto: string;
  dataEnvio?: Date; 
}