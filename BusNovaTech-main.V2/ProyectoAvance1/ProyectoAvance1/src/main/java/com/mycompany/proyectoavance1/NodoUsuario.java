//Author = Nyko
package com.mycompany.proyectoavance1;

public class NodoUsuario {
    private String nombreUsuario;
    private String contrasena;
    private NodoUsuario siguiente;

    public NodoUsuario(String nombreUsuario, String contrasena) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        siguiente = null;
    }

    public String getNombreUsuario(){ 
        return nombreUsuario;
    }
    public String getContrasena(){
        return contrasena; 
    }
    public NodoUsuario getSiguiente(){
        return siguiente; 
    }
    public void setSiguiente(NodoUsuario nodoSiguiente){ 
        siguiente = nodoSiguiente; 
    }
}