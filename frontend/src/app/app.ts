import { Component } from '@angular/core';
import { UsuariosComponent } from './components/usuarios/usuarios.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [UsuariosComponent],
  template: `<app-usuarios></app-usuarios>`,
  styleUrl: './app.css'
})
export class App {}
