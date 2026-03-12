////Authors = Allison Alvarado, Lafith Castrillo, Fernanda Herrera & Nicolas Peñas
package com.mycompany.proyectoavance1;

public class NodoTicket {
    private Ticket valor;
    private NodoTicket siguiente;

    public NodoTicket(Ticket v) {
        valor = v;
        siguiente = null;
    }

    public Ticket getValor() { 
        return valor; 
    }
    public NodoTicket getSiguiente() { 
        return siguiente;
    }
    public void setSiguiente(NodoTicket s) {
        siguiente = s;
    }
}
