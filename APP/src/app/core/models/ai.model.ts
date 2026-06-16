export interface PossoComprarRequest {
  item: string;
  valor: number;
}

export interface IaResponse {
  mensagem: string;
}

export interface SimulacaoRequest {
  valorCortadoMensal: number;
  meses: number;
  taxaJurosAnual: number; 
}

export interface SimulacaoResponse {
  valorTotalAcumulado: number;
  valorInvestido: number;
  jurosGanhos: number;
  evolucaoMensal: EvolucaoMes[]; 
}

export interface EvolucaoMes {
  mes: number;
  montante: number;
}