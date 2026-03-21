package com.mycompany.proyectoavance1;

public class AtendidosRepository {
    private String ruta;
    private ListaAtendidos lista;

    public AtendidosRepository(String rutaArchivo) {
        ruta = rutaArchivo;
        lista = new ListaAtendidos();
        cargarTickets();
    }

    public void agregarTicket(Ticket ticket) {
        if (ticket != null) {
            lista.agregar(ticket);
            guardarListaCompleta();
        }
    }

    public ListaAtendidos getLista() {
        return lista;
    }

    public void cargarTickets() {
        String contenido = JsonUtilSimple.leerArchivo(ruta);

        if (contenido == null || contenido.trim().equals("")) {
            return;
        }

        String linea = "";
        int i = 0;

        while (i < contenido.length()) {
            char c = contenido.charAt(i);

            if (c == '\n') {
                if (!linea.trim().equals("")) {
                    Ticket t = Ticket.fromJSONBloque(linea.trim());
                    if (t != null) {
                        lista.agregar(t);
                    }
                }
                linea = "";
            } else {
                linea += c;
            }

            i++;
        }

        if (!linea.trim().equals("")) {
            Ticket t = Ticket.fromJSONBloque(linea.trim());
            if (t != null) {
                lista.agregar(t);
            }
        }
    }

    public boolean guardarListaCompleta() {
        String contenido = "";
        NodoTicket actual = lista.getCabeza();

        while (actual != null) {
            contenido += actual.getValor().toJSON();

            if (actual.getSiguiente() != null) {
                contenido += "\n";
            }

            actual = actual.getSiguiente();
        }

        return JsonUtilSimple.escribirArchivo(ruta, contenido);
    }
  }
