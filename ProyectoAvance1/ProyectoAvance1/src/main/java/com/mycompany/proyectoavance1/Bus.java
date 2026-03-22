/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoavance1;

/**
 *
 * @author lcast
 */
public class Bus {
    private int numeroBus;
    private char tipoBus;
    private boolean inspectorOcupado;
    private Ticket ticketEnAtencion;
    private ListaTickets filaEspera;

    public Bus(int numeroBus, char tipoBus) {
        this.numeroBus = numeroBus;
        this.tipoBus = tipoBus;
        this.inspectorOcupado = false;
        this.ticketEnAtencion = null;
        this.filaEspera = new ListaTickets();
    }

    public int getNumeroBus() {
        return numeroBus;
    }

    public char getTipoBus() {
        return tipoBus;
    }

    public boolean isInspectorOcupado() {
        return inspectorOcupado;
    }

    public Ticket getTicketEnAtencion() {
        return ticketEnAtencion;
    }

    public ListaTickets getFilaEspera() {
        return filaEspera;
    }

    public boolean estaLibre() {
        return !inspectorOcupado && ticketEnAtencion == null;
    }

    public void asignarAtencionDirecta(Ticket ticket) {
        if (ticket == null) {
            return;
        }

        ticketEnAtencion = ticket;
        inspectorOcupado = true;
        ticketEnAtencion.marcarEnAtencion();
    }

    public void agregarAFila(Ticket ticket) {
        if (ticket == null) {
            return;
        }

        filaEspera.agregar(ticket);
    }

    public Ticket finalizarAtencion() {
        Ticket ticketFinalizado = ticketEnAtencion;
        ticketEnAtencion = null;
        inspectorOcupado = false;
        return ticketFinalizado;
    }

    public int cantidadEnFila() {
        return filaEspera.tamano();
    }
    
    public boolean estaCompletamenteVacio() {
    return ticketEnAtencion == null && filaEspera.estaVacia();
    }

    public String resumenBus() {
        String texto = "Bus #" + numeroBus + " | Tipo=" + tipoBus;

        if (inspectorOcupado && ticketEnAtencion != null) {
            texto += " | En atencion: " + ticketEnAtencion.getNombre();
        } else {
            texto += " | Sin atencion actual";
        }

        texto += " | En fila: " + filaEspera.tamano();

        return texto;
    }
}
