//Author = Nyko
package com.mycompany.proyectoavance1;
public class ListaTickets {
    private NodoTicketRepo cabeza;
    private int tamano;

    public ListaTickets() {
        cabeza = null;
        tamano = 0;
    }

    public void agregar(Ticket ticket) {
        if (ticket == null) return;
        NodoTicketRepo nuevoNodo = new NodoTicketRepo(ticket);
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            NodoTicketRepo actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevoNodo);
        }
        tamano++;
    }

    public Ticket obtener(int posicion) {
        if (posicion < 0 || posicion >= tamano) return null;
        NodoTicketRepo actual = cabeza;
        int indice = 0;
        while (indice < posicion) {
            actual = actual.getSiguiente();
            indice++;
        }
        return actual.getValor();
    }

    public void reemplazar(int posicion, Ticket ticket) {
        if (posicion < 0 || posicion >= tamano) return;
        NodoTicketRepo actual = cabeza;
        int indice = 0;
        while (indice < posicion) {
            actual = actual.getSiguiente();
            indice++;
        }
        actual.setValor(ticket);
    }
    public Ticket sacarPrimero() {
    if (cabeza == null) {
        return null;
    }

    Ticket ticketPrimero = cabeza.getValor();
    cabeza = cabeza.getSiguiente();
    tamano--;
    return ticketPrimero;
    }

    public int tamano(){ 
        return tamano; 
    }
    public boolean estaVacia() {
    return cabeza == null;
    }

    public NodoTicketRepo getCabeza()
    { return cabeza; 
    }
}
