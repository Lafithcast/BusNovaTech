/**
 * Repositorio encargado de la administración de tickets
 * almacenados en un archivo JSON.
 * <p>
 * Permite cargar, guardar, agregar y actualizar tickets
 * dentro del archivo de persistencia del sistema.
 * </p>
 */
package com.mycompany.proyectoavance1;
public final class TicketRepository {
    public final String ruta;
    public ListaTickets cache;

    public TicketRepository(String rutaArchivo) {
        ruta = rutaArchivo;
        cache = new ListaTickets();
        cargarTickets();
    }

    public void cargarTickets() {
        String contenido = JsonUtilSimple.leerArchivo(ruta);

        if (contenido == null || contenido.trim().isEmpty()) {
            return;
        }

        String linea = "";
        int i = 0;

        while (i < contenido.length()) {
            char c = contenido.charAt(i);

            if (c == '\n') {
                procesarLinea(linea);
                linea = "";
            } else if (c != '\r') {
                linea += c;
            }

            i++;
        }

        if (!linea.trim().equals("")) {
            procesarLinea(linea);
        }
    }

    private void procesarLinea(String linea) {
        if (linea == null || linea.trim().equals("")) {
            return;
        }

        Ticket t = Ticket.fromJSONBloque(linea.trim());

        if (t != null) {
            cache.agregar(t);
        }
    }

    public void agregarTicket(Ticket t) {
        if (t == null) {
            return;
        }

        cache.agregar(t);
        guardarListaCompleta();
    }

    public void actualizarTicket(Ticket actualizado) {
        if (actualizado == null) {
            return;
        }

        NodoTicket actual = cache.getCabeza();

        while (actual != null) {
            Ticket ticket = actual.getValor();

            if (ticket != null) {
                if (ticket.getId() == actualizado.getId() &&
                    mismoTexto(ticket.getHoraCompra(), actualizado.getHoraCompra())) {

                    ticket.nombre = actualizado.nombre;
                    ticket.id = actualizado.id;
                    ticket.edad = actualizado.edad;
                    ticket.monedaCuenta = actualizado.monedaCuenta;
                    ticket.horaCompra = actualizado.horaCompra;
                    ticket.horaAbordaje = actualizado.horaAbordaje;
                    ticket.servicio = actualizado.servicio;
                    ticket.tipoBus = actualizado.tipoBus;
                    ticket.estado = actualizado.estado;
                    ticket.terminalCompra = actualizado.terminalCompra;
                    ticket.busAsignado = actualizado.busAsignado;
                    ticket.librasCarga = actualizado.librasCarga;

                    guardarListaCompleta();
                    return;
                }
            }

            actual = actual.getSiguiente();
        }

        cache.agregar(actualizado);
        guardarListaCompleta();
    }

    public boolean guardarListaCompleta() {
        String contenido = "";
        NodoTicket actual = cache.getCabeza();

        while (actual != null) {
            Ticket t = actual.getValor();
            contenido += t.toJSON();

            if (actual.getSiguiente() != null) {
                contenido += "\n";
            }

            actual = actual.getSiguiente();
        }

        return JsonUtilSimple.escribirArchivo(ruta, contenido);
    }

    public boolean mismoTexto(String a, String b) {
        if (a == null && b == null) {
            return true;
        }

        if (a == null) {
            return false;
        }

        return a.equals(b);
    }
}
