import { Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.css',
})
export class MainLayout {
  private router = inject(Router);

  logout(): void {
    this.router.navigate(['/login']);
  }

  readonly nomeUsuario = signal('Júlia Pimentel'); 

  readonly iniciaisUsuario = computed(() => {
    const nome = this.nomeUsuario();
    if (!nome) return 'US'; 

    const partes = nome.trim().split(' ');
    if (partes.length === 1) {
      return partes[0].substring(0, 2).toUpperCase();
    }
    return (partes[0][0] + partes[partes.length - 1][0]).toUpperCase();
  });
}