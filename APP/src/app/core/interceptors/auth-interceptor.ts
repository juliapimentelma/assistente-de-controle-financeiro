import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = authService.getToken();

  const rotasPublicas = ['/api/auth/login', '/api/auth/registrar'];
  const isRotaPublica = rotasPublicas.some(url => req.url.includes(url));

  let requisicaoFinal = req;

  if (token && !isRotaPublica) {
    requisicaoFinal = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(requisicaoFinal).pipe(
    catchError((erro: HttpErrorResponse) => {

      if (erro.status === 401 || erro.status === 403) {

        authService.logout();

        router.navigate(['/login']);
      }

      return throwError(() => erro);
    })
  );
};