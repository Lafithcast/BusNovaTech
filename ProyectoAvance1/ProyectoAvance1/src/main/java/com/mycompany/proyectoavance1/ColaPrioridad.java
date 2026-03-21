/**
 * Estructura encargada de administrar tickets en colas
 * según su prioridad o tipoBusTicket de bus.
 * <p>
 * El sistema utiliza tres colas:
 * preferencial, directo y normal.
 * Al momento de atender, siempre se da prioridad
 * primero a preferencial, luego directo y finalmente normal.
 * </p>
 */
package com.mycompany.proyectoavance1;
public class ColaPrioridad {

    private NodoTicket cabezaPreferencial, colaPreferencial;
    private NodoTicket cabezaDirecto, colaDirecto;
    private NodoTicket cabezaNormal, colaNormal;
    private int tamanoColaPreferencial, tamanoColaDirecto, tamanoColaNormal;

    public ColaPrioridad() {
        tamanoColaPreferencial = 0; tamanoColaDirecto = 0; tamanoColaNormal = 0;
    }

    public void encolar(Ticket ticket) {
        if (ticket == null){
            return;
        }
        char tipoBusTicket = ticket.getTipoBus();
        if (tipoBusTicket == 'P'){
            encolarPreferencial(ticket); 
        }else if (tipoBusTicket == 'D') {
            encolarDirecto(ticket);
        }else{
            encolarNormal(ticket);
        }
        
    }

    public Ticket desencolar() {
        if (cabezaPreferencial != null) 
            return desencolarP();
        if (cabezaDirecto != null) 
            return desencolarD();
        if (cabezaNormal != null) 
            return desencolarN();
        return null;
    }

    public int tamanoPreferencial() { return tamanoColaPreferencial; }
    public int tamanoDirecto() { return tamanoColaDirecto; }
    public int tamanoNormal() { return tamanoColaNormal; }

    public String vistaPrevia(int cantidadMaxima) {
        String string = "";
        string += "Preferencial:\nuevoNodo" + obtenerTopCola(cabezaPreferencial, cantidadMaxima);
        string += "Directo:\nuevoNodo" + obtenerTopCola(cabezaDirecto, cantidadMaxima);
        string += "Normal:\nuevoNodo" + obtenerTopCola(cabezaNormal, cantidadMaxima);
        return string;
    }

    private String obtenerTopCola(NodoTicket cabeza, int cantidadMaxima) {
        String resultado = "";
        int contador = 0;
        NodoTicket nodoActual = cabeza;

        while (nodoActual != null && contador < cantidadMaxima) {
            resultado += " - " + nodoActual.getValor().resumen() + "\nuevoNodo";
            nodoActual = nodoActual.getSiguiente();
            contador++;
        }

        if (contador == 0){ 
            resultado += " - (vacio)\nuevoNodo";
        }
            return resultado;
    }

    private void encolarPreferencial(Ticket ticket) {
        NodoTicket nuevoNodo = new NodoTicket(ticket);
        if (cabezaPreferencial == null){
            cabezaPreferencial = colaPreferencial = nuevoNodo; 
        }else{ 
            colaPreferencial.setSiguiente(nuevoNodo); colaPreferencial = nuevoNodo; }
        tamanoColaPreferencial++;
    }

    private void encolarDirecto(Ticket ticket) {
        NodoTicket nuevoNodo = new NodoTicket(ticket);
        if (cabezaDirecto == null) { 
            cabezaDirecto = colaDirecto = nuevoNodo; 
        }else{ 
            colaDirecto.setSiguiente(nuevoNodo); colaDirecto = nuevoNodo; 
            }
        tamanoColaDirecto++;
    }

    private void encolarNormal(Ticket ticket) {
        NodoTicket nuevoNodo = new NodoTicket(ticket);
        if (cabezaNormal == null) {
            cabezaNormal = colaNormal = nuevoNodo;
        } else { 
            colaNormal.setSiguiente(nuevoNodo); colaNormal = nuevoNodo;
        }
        tamanoColaNormal++;
    }

    private Ticket desencolarP() {
        Ticket ticket = cabezaPreferencial.getValor();
        cabezaPreferencial = cabezaPreferencial.getSiguiente();
        if (cabezaPreferencial == null) {
            colaPreferencial = null;
        }
        tamanoColaPreferencial--;
        return ticket;
    }

    private Ticket desencolarD() {
        Ticket ticket = cabezaDirecto.getValor();
        cabezaDirecto = cabezaDirecto.getSiguiente();
        if (cabezaDirecto == null){
            colaDirecto = null;
        }
        tamanoColaDirecto--;
        return ticket;
    }

    private Ticket desencolarN() {
        Ticket ticket = cabezaNormal.getValor();
        cabezaNormal = cabezaNormal.getSiguiente();
        if (cabezaNormal == null){
            colaNormal = null;
        }
        tamanoColaNormal--;
        return ticket;
    }
}