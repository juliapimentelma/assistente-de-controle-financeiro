import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ExtratoService {
  private readonly http = inject(HttpClient);
  // Ajuste para a rota do Controller responsável pelos relatórios no seu Spring Boot
  private readonly API_URL = 'http://localhost:8080/api/extratos'; 

  baixarPdf(mes: number, ano: number): Observable<Blob> {
    const params = new HttpParams()
      .set('mes', mes.toString())
      .set('ano', ano.toString());

    // O responseType 'blob' é fundamental para converter o arquivo que vem do Java em algo baixável no navegador
    return this.http.get(`${this.API_URL}/pdf`, { 
      params, 
      responseType: 'blob' 
    });
  }
}