import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AporteRequest, MetaRequest, MetaResponse } from '../models/meta.model';

@Injectable({
  providedIn: 'root',
})

export class MetaService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/metas'; 

  listarTodas(): Observable<MetaResponse[]> {
    return this.http.get<MetaResponse[]>(this.apiUrl);
  }

  criarMeta(request: MetaRequest): Observable<MetaResponse> {
    return this.http.post<MetaResponse>(this.apiUrl, request);
  }

  adicionarAporte(id: number, aporte: AporteRequest): Observable<MetaResponse> {
    return this.http.patch<MetaResponse>(`${this.apiUrl}/${id}/aporte`, aporte);
  }
}