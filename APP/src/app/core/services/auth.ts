import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly TOKEN_KEY = 'conselheiro_jwt';

  constructor() { }

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

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }
}