import { Component, OnInit } from '@angular/core';
import { UsuariosService, Usuario } from '../../services/usuarios';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

declare var bootstrap: any;

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './usuarios.component.html',
  styleUrls: ['./usuarios.component.css']
})
export class UsuariosComponent implements OnInit {
  usuarios: Usuario[] = [];
  usuarioActual: Usuario = { nombre: '', email: '', edad: 0 };

  constructor(private usuariosService: UsuariosService) {}

  ngOnInit(): void {
  this.usuariosService.getUsuarios().subscribe({
    next: (data) => {
      this.usuarios = [...data].sort((a, b) => (a.id ?? 0) - (b.id ?? 0));
    },
    error: (err) => console.error('Error al cargar usuarios', err),
  });
}

  //Cargar lista completa
  cargarUsuarios(): void {
  this.usuariosService.getUsuarios().subscribe({
    next: (data) => {
      //console.log('Usuarios cargados:', data);
      this.usuarios = data;
    },
    error: (err) => console.error('Error al cargar usuarios', err),
  });
}

  //Crear usuario
  crearUsuario(): void {
    if (!this.usuarioActual.nombre || !this.usuarioActual.email || !this.usuarioActual.edad) return;

    this.usuariosService.createUsuario(this.usuarioActual).subscribe({
      next: () => {
        this.cerrarModal('#modalAgregar');
        this.resetFormulario();
        this.cargarUsuarios();
        window.location.reload();
      },
      error: (err) => console.error('Error al crear usuario', err),
    });
  }

  //Seleccionar usuario para modal editar/eliminar
  seleccionarUsuario(usuario: Usuario): void {
    this.usuarioActual = { ...usuario };
  }

  //Actualizar usuario
  actualizarUsuario(): void {
    console.log('crearUsuario ejecutado');
    if (!this.usuarioActual.id) return;

    this.usuariosService.updateUsuario(this.usuarioActual.id, this.usuarioActual).subscribe({
      next: () => {
        this.cerrarModal('#modalEditar');
        this.resetFormulario();
        this.cargarUsuarios();
        window.location.reload();
      },
      error: (err) => console.error('Error al actualizar usuario', err),
    });
  }

  //Eliminar usuario
  eliminarUsuario(): void {
    if (!this.usuarioActual.id) return;

    this.usuariosService.deleteUsuario(this.usuarioActual.id).subscribe({
      next: () => {
        this.cerrarModal('#modalEliminar');
        this.cargarUsuarios();
        window.location.reload();
      },
      error: (err) => console.error('Error al eliminar usuario', err),
    });
  }

  //Reset formulario
  resetFormulario(): void {
    this.usuarioActual = { nombre: '', email: '', edad: 0 };
  }

  cerrarModal(selector: string): void {
  const modalEl = document.querySelector(selector);
  if (modalEl) {
    let modalInstance = bootstrap.Modal.getInstance(modalEl);
    if (!modalInstance) {
      modalInstance = new bootstrap.Modal(modalEl);
    }
    modalInstance.hide();
      setTimeout(() => {
        document.querySelectorAll('.modal-backdrop').forEach(bd => bd.remove());
        (document.activeElement as HTMLElement)?.blur();
      }, 400);
    }
  }
}
