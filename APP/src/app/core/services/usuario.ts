import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UsuarioPerfilResponse, AtualizarPerfilRequest, AtualizarCredenciaisRequest } from '../models/usuario.model';

@Injectable({
  providedIn: 'root',
})

export class UsuarioService {

  private readonly API_URL = 'http://localhost:8080/api/usuarios/me';
  private http = inject(HttpClient);

  obterMeuPerfil(): Observable<UsuarioPerfilResponse> {
    return this.http.get<UsuarioPerfilResponse>(this.API_URL);
  }

  atualizarPerfil(dados: AtualizarPerfilRequest): Observable<UsuarioPerfilResponse> {
    return this.http.put<UsuarioPerfilResponse>(this.API_URL, dados);
  }

  atualizarCredenciais(dados: AtualizarCredenciaisRequest): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/credenciais`, dados);
  }

  desativarMinhaConta(): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/desativar`);
  }
}
