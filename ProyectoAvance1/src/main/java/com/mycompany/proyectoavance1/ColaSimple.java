package com.mycompany.proyectoavance1;

public class ColaSimple {
    private NodoTicket head, tail;
    private int size;

    public ColaSimple() {
        size = 0;
    }

    public void encolar(Ticket ticket) {
        if (ticket == null) return;
        NodoTicket n = new NodoTicket(ticket);
        if (head == null) {
            head = tail = n;
        } else {
            tail.setSiguiente(n);
            tail = n;
        }
        size++;
    }

    public Ticket desencolar() {
        if (head == null) return null;
        Ticket ticket = head.getValor();
        head = head.getSiguiente();
        if (head == null) tail = null;
        size--;
        return ticket;
    }

    public int tamano() {
        return size;
    }
}