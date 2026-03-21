/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoavance1;

/**
 *
 * @author lcast
 */
public class NodoBus {
    private Bus valor;
    private NodoBus siguiente;

    public NodoBus(Bus v) {
        valor = v;
        siguiente = null;
    }

    public Bus getValor() {
        return valor;
    }

    public NodoBus getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoBus s) {
        siguiente = s;
    }
}
