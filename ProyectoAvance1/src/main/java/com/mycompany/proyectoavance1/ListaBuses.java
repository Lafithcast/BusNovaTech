/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoavance1;

/**
 *
 * @author lcast
 */
public class ListaBuses {
    private NodoBus cabeza;
    private NodoBus ultimo;
    private int size;

    public ListaBuses() {
        cabeza = null;
        ultimo = null;
        size = 0;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public void agregar(Bus bus) {
        if (bus == null) {
            return;
        }

        NodoBus nuevo = new NodoBus(bus);

        if (cabeza == null) {
            cabeza = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.setSiguiente(nuevo);
            ultimo = nuevo;
        }

        size++;
    }

    public int tamano() {
        return size;
    }

    public NodoBus getCabeza() {
        return cabeza;
    }

    public Bus buscarPorId(int id) {
        NodoBus actual = cabeza;

        while (actual != null) {
            Bus b = actual.getValor();

            if (b != null && b.getId() == id) {
                return b;
            }

            actual = actual.getSiguiente();
        }

        return null;
    }
}
