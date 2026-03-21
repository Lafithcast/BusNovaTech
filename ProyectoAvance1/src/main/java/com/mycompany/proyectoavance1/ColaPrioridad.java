/**
 * Estructura encargada de administrar tickets en colas
 * según su prioridad o tipo de bus.
 * <p>
 * El sistema utiliza tres colas:
 * preferencial, directo y normal.
 * Al momento de atender, siempre se da prioridad
 * primero a preferencial, luego directo y finalmente normal.
 * </p>
 */
package com.mycompany.proyectoavance1;
public class ColaPrioridad {

    private NodoTicket headP, tailP;
    private NodoTicket headD, tailD;
    private NodoTicket headN, tailN;

    private int sizeP, sizeD, sizeN;

    public ColaPrioridad() {
        sizeP = 0; sizeD = 0; sizeN = 0;
    }

    public void encolar(Ticket t) {
        if (t == null) 
            return;
            char tipo = t.getTipoBus();
        if (tipo == 'P')
            encolarP(t);
        else if (tipo == 'D') 
            encolarD(t);
        else encolarN(t);
    }

    public Ticket desencolar() {
        if (headP != null) 
            return desencolarP();
        if (headD != null) 
            return desencolarD();
        if (headN != null) 
            return desencolarN();
        return null;
    }

    public int tamanoPreferencial() { return sizeP; }
    public int tamanoDirecto() { return sizeD; }
    public int tamanoNormal() { return sizeN; }

    public String vistaPrevia(int max) {
        String s = "";
        s += "P:\n" + top(headP, max);
        s += "D:\n" + top(headD, max);
        s += "N:\n" + top(headN, max);
        return s;
    }

    private String top(NodoTicket head, int max) {
        String s = "";
        int c = 0;
        NodoTicket aux = head;

        while (aux != null && c < max) {
            s += " - " + aux.getValor().resumen() + "\n";
            aux = aux.getSiguiente();
            c++;
        }

        if (c == 0) s += " - (vacío)\n";
            return s;
    }

    private void encolarP(Ticket ticket) {
        NodoTicket nodo = new NodoTicket(ticket);
        if (headP == null){
            headP = tailP = nodo; 
        }else{ 
            tailP.setSiguiente(nodo); tailP = nodo; }
        sizeP++;
    }

    private void encolarD(Ticket ticket) {
        NodoTicket nodo = new NodoTicket(ticket);
        if (headD == null) { 
            headD = tailD = nodo; 
        }else{ 
            tailD.setSiguiente(nodo); tailD = nodo; 
            }
        sizeD++;
    }

    private void encolarN(Ticket ticket) {
        NodoTicket nodo = new NodoTicket(ticket);
        if (headN == null) {
            headN = tailN = nodo;
        } else { 
            tailN.setSiguiente(nodo); tailN = nodo;
        }
        sizeN++;
    }

    private Ticket desencolarP() {
        Ticket ticket = headP.getValor();
        headP = headP.getSiguiente();
        if (headP == null) 
            tailP = null;
            sizeP--;
            return ticket;
    }

    private Ticket desencolarD() {
        Ticket ticket = headD.getValor();
        headD = headD.getSiguiente();
        if (headD == null) 
            tailD = null;
        sizeD--;
            return ticket;
    }

    private Ticket desencolarN() {
        Ticket ticket = headN.getValor();
        headN = headN.getSiguiente();
        if (headN == null) 
            tailN = null;
            sizeN--;
            return ticket;
    }
}