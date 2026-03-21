package com.mycompany.proyectoavance1;

public class ColaSimple {
    private NodoTicket head, tail;
    private int size;

    public ColaSimple() {
        size = 0;
    }

    public void encolar(Ticket t) {
        if (t == null) return;
        NodoTicket n = new NodoTicket(t);
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
        Ticket t = head.getValor();
        head = head.getSiguiente();
        if (head == null) tail = null;
        size--;
        return t;
    }

    public int tamano() {
        return size;
    }
}