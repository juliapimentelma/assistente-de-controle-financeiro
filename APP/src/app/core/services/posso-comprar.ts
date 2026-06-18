import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IaResponse, PossoComprarRequest } from '../models/posso-comprar';

@Injectable({
  providedIn: 'root',
})
export class PossoComprarService {

  private readonly http = inject(HttpClient);
  
  private readonly apiUrl = 'http://localhost:8080/api/conselheiro/posso-comprar';

  consultarIA(request: PossoComprarRequest): Observable<IaResponse> {
    return this.http.post<IaResponse>(this.apiUrl, request);
  }
}