import { Routes } from '@angular/router';
import { Login } from './pages/auth/login/login';
import { Cadastro } from './pages/auth/cadastro/cadastro';
import { Dashboard } from './pages/dashboard/dashboard';
import { authGuard } from './core/guards/auth-guard';
import { MainLayout } from './layout/main-layout';
import { Metas } from './pages/metas/metas';
import { Transacoes } from './pages/transacoes/transacoes';
import { Orcamentos } from './pages/orcamentos/orcamentos';
import { PossoComprar } from './pages/posso-comprar/posso-comprar';

export const routes: Routes = [
  { 
    path: 'login', 
    component: Login 
  },
  { 
    path: 'cadastro', 
    component: Cadastro 
  },
  {
    path: '',
    component: MainLayout,
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', component: Dashboard },
      { path: 'metas', component: Metas},
      
      { 
      path: 'extrato', 
      title: 'Meu Extrato',
      component: Transacoes 
      },
      { path: 'orcamentos', component: Orcamentos },
      {path: 'posso-comprar', title: 'Posso Comprar? - Conselheiro IA', component: PossoComprar},
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: 'login' }
];