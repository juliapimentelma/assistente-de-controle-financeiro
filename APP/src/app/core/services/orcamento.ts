import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OrcamentoRequest, OrcamentoResponse } from '../models/orcamento.model';

@Injectable({
  providedIn: 'root',
})
export class OrcamentoService {

  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/orcamentos'; 

  listarPorCompetencia(mes: number, ano: number): Observable<OrcamentoResponse[]> {
    const params = new HttpParams()
      .set('mes', mes.toString())
      .set('ano', ano.toString());

    return this.http.get<OrcamentoResponse[]>(this.apiUrl, { params });
  }

  definirOrcamento(request: OrcamentoRequest): Observable<OrcamentoResponse> {
    return this.http.put<OrcamentoResponse>(this.apiUrl, request);
  }
}