package com.mycompany.proyectoavance1;

public class AtendidosRepository {
    private String ruta;
    private ListaAtendidos lista;

    public AtendidosRepository(String rutaArchivo) {
        ruta = rutaArchivo;
        lista = new ListaAtendidos();
        cargarTickets();
    }

    public void agregarTicket(Ticket t) {
        if (t != null) {
            lista.agregar(t);
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

        String[] lineas = contenido.split("\n");
        int i = 0;

        while (i < lineas.length) {
            String linea = lineas[i].trim();

            if (!linea.equals("")) {
                Ticket t = Ticket.fromJSONBloque(linea);
                if (t != null) {
                    lista.agregar(t);
                }
            }

            i++;
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
