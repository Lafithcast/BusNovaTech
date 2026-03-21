/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoavance1;

/**
 *
 * @author lcast
 */
public class ListaTickets {
    private NodoTicket cabeza;
    private NodoTicket ultimo;
    private int size;

    public ListaTickets() {
        cabeza = null;
        ultimo = null;
        size = 0;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public void agregar(Ticket t) {
        if (t == null) {
            return;
        }

        NodoTicket nuevo = new NodoTicket(t);

        if (cabeza == null) {
            cabeza = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.setSiguiente(nuevo);
            ultimo = nuevo;
        }

        size++;
    }

    public NodoTicket getCabeza() {
        return cabeza;
    }

    public int tamano() {
        return size;
    }
}
