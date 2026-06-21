import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TransacaoRequest, TransacaoResponse, PageResponse } from '../models/transacao.model';

@Injectable({
  providedIn: 'root',
})
export class TransacaoService {
  private readonly http = inject(HttpClient);

  private readonly API_URL = 'http://localhost:8080/api/transacoes'; 

  criar(request: TransacaoRequest): Observable<TransacaoResponse> {
    return this.http.post<TransacaoResponse>(this.API_URL, request);
  }

  listarPorCompetencia(mes: number, ano: number, page: number = 0, size: number = 20): Observable<PageResponse<TransacaoResponse>> {
    let params = new HttpParams()
      .set('mes', mes.toString())
      .set('ano', ano.toString())
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PageResponse<TransacaoResponse>>(this.API_URL, { params });
  }

  marcarComoPago(id: number): Observable<void> {
    return this.http.patch<void>(`${this.API_URL}/${id}/pagar`, {});
  }

  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  listarPorGrupo(grupoId: string): Observable<TransacaoResponse[]> {
    return this.http.get<TransacaoResponse[]>(`${this.API_URL}/grupo/${grupoId}`);
  }
}