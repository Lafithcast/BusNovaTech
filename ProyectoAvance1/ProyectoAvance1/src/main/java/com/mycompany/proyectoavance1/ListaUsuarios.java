//Author = nyko
package com.mycompany.proyectoavance1;

public class ListaUsuarios {
    private NodoUsuario cabeza;
    private int tamano;

    public ListaUsuarios() {
        cabeza = null;
        tamano = 0;
    }

    public void agregar(String nombreUsuario, String contrasena) {
        NodoUsuario nuevoNodo = new NodoUsuario(nombreUsuario, contrasena);
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            NodoUsuario actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevoNodo);
        }
        tamano++;
    }

    public boolean existeUsuario(String nombreUsuario) {
        NodoUsuario actual = cabeza;
        while (actual != null) {
            if (actual.getNombreUsuario().equals(nombreUsuario)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public boolean validarLogin(String nombreUsuario, String contrasena) {
        NodoUsuario actual = cabeza;
        while (actual != null) {
            if (actual.getNombreUsuario().equals(nombreUsuario)
                && actual.getContrasena().equals(contrasena)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public int tamano(){ 
        return tamano; 
    }

    public NodoUsuario getCabeza() {
        return cabeza;
    }
}