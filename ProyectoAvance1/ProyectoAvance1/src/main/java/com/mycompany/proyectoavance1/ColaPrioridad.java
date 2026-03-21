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

    private String top(NodoTicket h, int max) {
        String s = "";
        int c = 0;
        NodoTicket aux = h;

        while (aux != null && c < max) {
            s += " - " + aux.getValor().resumen() + "\n";
            aux = aux.getSiguiente();
            c++;
        }

        if (c == 0) s += " - (vacío)\n";
            return s;
    }

    private void encolarP(Ticket t) {
        NodoTicket n = new NodoTicket(t);
        if (headP == null){
            headP = tailP = n; 
        }else{ 
            tailP.setSiguiente(n); tailP = n; }
        sizeP++;
    }

    private void encolarD(Ticket t) {
        NodoTicket n = new NodoTicket(t);
        if (headD == null) { 
            headD = tailD = n; 
        }else{ 
            tailD.setSiguiente(n); tailD = n; 
            }
        sizeD++;
    }

    private void encolarN(Ticket t) {
        NodoTicket n = new NodoTicket(t);
        if (headN == null) {
            headN = tailN = n;
        } else { 
            tailN.setSiguiente(n); tailN = n;
        }
        sizeN++;
    }

    private Ticket desencolarP() {
        Ticket t = headP.getValor();
        headP = headP.getSiguiente();
        if (headP == null) 
            tailP = null;
            sizeP--;
            return t;
    }

    private Ticket desencolarD() {
        Ticket t = headD.getValor();
        headD = headD.getSiguiente();
        if (headD == null) 
            tailD = null;
        sizeD--;
            return t;
    }

    private Ticket desencolarN() {
        Ticket t = headN.getValor();
        headN = headN.getSiguiente();
        if (headN == null) 
            tailN = null;
            sizeN--;
            return t;
    }
}