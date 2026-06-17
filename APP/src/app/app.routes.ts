import { Routes } from '@angular/router';
import { Login } from './pages/auth/login/login';
import { Cadastro } from './pages/auth/cadastro/cadastro';
import { Dashboard } from './pages/dashboard/dashboard';
import { authGuard } from './core/guards/auth-guard';
import { MainLayout } from './layout/main-layout';
import { Metas } from './pages/metas/metas';

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
      
      // ROTAS FUTURAS (Vamos descomentando conforme formos criando)
      // { path: 'extrato', component: ExtratoComponent },
      // { path: 'orcamentos', component: OrcamentosComponent },
      // { path: 'metas', component: MetasComponent },
      // { path: 'simulacao', component: SimulacaoComponent },
      // { path: 'ia', component: IaChatComponent },
      
      // Rota padrão: se o usuário logar e a URL estiver vazia, joga pro dashboard!
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: 'login' }
];