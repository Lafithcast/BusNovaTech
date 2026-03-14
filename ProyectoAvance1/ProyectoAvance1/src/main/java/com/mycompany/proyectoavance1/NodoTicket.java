/**
 * Representa un nodo dentro de una estructura enlazada de tickets.
 * <p>
 * Cada nodo almacena un objeto {@link Ticket} y una referencia
 * al siguiente nodo de la secuencia.
 * </p>
 */
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
