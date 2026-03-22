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
    private int tamano;
    
    public int obtenerNumeroMayorBus() {
        if (cabeza == null) {
            return 0;
    }

    int numeroMayor = 0;
    NodoBus actual = cabeza;

    while (actual != null) {
        if (actual.getBus().getNumeroBus() > numeroMayor) {
            numeroMayor = actual.getBus().getNumeroBus();
        }
        actual = actual.getSiguiente();
    }

    return numeroMayor;
    }
    
    public Bus obtenerUltimoBus() {
        if (cabeza == null) {
            return null;
    }

    NodoBus actual = cabeza;

    while (actual.getSiguiente() != null) {
        actual = actual.getSiguiente();
    }

    return actual.getBus();
    }
    
    public boolean eliminarUltimoBus() {
        if (cabeza == null) {
            return false;
    }

    if (cabeza.getSiguiente() == null) {
        cabeza = null;
        tamano--;
        return true;
    }

    NodoBus actual = cabeza;

    while (actual.getSiguiente().getSiguiente() != null) {
        actual = actual.getSiguiente();
    }

    actual.setSiguiente(null);
    tamano--;
    return true;
    }

    public ListaBuses() {
        cabeza = null;
        tamano = 0;
    }

    public void agregarBus(Bus bus) {
        if (bus == null) {
            return;
        }

        NodoBus nuevoNodo = new NodoBus(bus);

        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            NodoBus actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevoNodo);
        }

        tamano++;
    }

    public Bus buscarBusPorNumero(int numeroBus) {
        NodoBus actual = cabeza;

        while (actual != null) {
            if (actual.getBus().getNumeroBus() == numeroBus) {
                return actual.getBus();
            }
            actual = actual.getSiguiente();
        }

        return null;
    }

    public Bus obtenerBusEnPosicion(int posicion) {
        if (posicion < 0 || posicion >= tamano) {
            return null;
        }

        NodoBus actual = cabeza;
        int indice = 0;

        while (indice < posicion) {
            actual = actual.getSiguiente();
            indice++;
        }

        return actual.getBus();
    }

    public int tamano() {
        return tamano;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public String mostrarResumen() {
        if (cabeza == null) {
            return "No hay buses registrados.";
        }

        String texto = "";
        NodoBus actual = cabeza;

        while (actual != null) {
            texto += actual.getBus().resumenBus() + "\n";
            actual = actual.getSiguiente();
        }

        return texto;
    }
}