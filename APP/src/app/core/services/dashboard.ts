import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DashboardResponse } from '../models/dashboard.model';

@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  private http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8080/api/dashboard';

  getResumo(): Observable<DashboardResponse> {
    return this.http.get<DashboardResponse>(`${this.API_URL}/resumo`);
  }
}