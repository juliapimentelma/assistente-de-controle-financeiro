import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CategoriaRequest, CategoriaResponse } from '../models/categoria.model';

@Injectable({
  providedIn: 'root',
})
export class CategoriaService {
  private readonly http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8080/api/categorias'; 

  listarTodas(): Observable<CategoriaResponse[]> {
    return this.http.get<CategoriaResponse[]>(this.API_URL);
  }
  
  listarPorTipo(tipo: 'RECEITA' | 'DESPESA'): Observable<CategoriaResponse[]> {
    return this.http.get<CategoriaResponse[]>(`${this.API_URL}?tipo=${tipo}`);
  }

  criar(request: CategoriaRequest): Observable<CategoriaResponse> {
    return this.http.post<CategoriaResponse>(this.API_URL, request);
  }
}