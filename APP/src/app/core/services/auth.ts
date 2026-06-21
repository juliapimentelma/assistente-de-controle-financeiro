import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { CadastroResponse, CadastroRequest, LoginRequest, LoginResponse } from '../models/auth.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly TOKEN_KEY = 'conselheiro_jwt';
  private readonly API_URL = 'http://localhost:8080/api/auth'; 
  
  private http = inject(HttpClient);


  login(credenciais: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.API_URL}/login`, credenciais).pipe(
      tap(response => {
        if (response.token) {
          this.salvarToken(response.token);
        }
      })
    );
  }

  salvarToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isAutenticado(): boolean {
    const token = this.getToken();
    return token !== null && token !== ''; 
  }

  cadastrar(dados: CadastroRequest): Observable<CadastroResponse> {
    return this.http.post<CadastroResponse>(`${this.API_URL}/registrar`, dados);
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }
}