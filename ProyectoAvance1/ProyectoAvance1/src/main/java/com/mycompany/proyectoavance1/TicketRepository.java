/**
 * Repositorio encargado de la administración de tickets
 * almacenados en un archivo JSON.
 * <p>
 * Permite cargar, guardar, agregar y actualizar tickets
 * dentro del archivo de persistencia del sistema.
 * </p>
 */
package com.mycompany.proyectoavance1;
public class TicketRepository {

    private String ruta;
    private ListaTickets cache;

    public TicketRepository(String rutaArchivo) {
        ruta = rutaArchivo;
        cache = new ListaTickets();

        ListaTickets cargados = cargarTickets();
        int indice = 0;
        while (indice < cargados.tamano()) {
            Ticket ticketCargado = cargados.obtener(indice);
            if (ticketCargado != null) {
                cache.agregar(ticketCargado);
            }
            indice++;
        }
    }

    public ListaTickets cargarTickets() {
        ListaTickets listaResultado = new ListaTickets();
        String json = JsonUtilSimple.leerArchivo(ruta);
        if (json == null) return listaResultado;

        int posicionBusqueda = 0;
        while (true) {
            int inicioObjeto = json.indexOf("{\"nombre\"", posicionBusqueda);
            if (inicioObjeto < 0) break;

            int finObjeto = json.indexOf("}", inicioObjeto);
            if (finObjeto < 0) break;

            String bloqueJson = json.substring(inicioObjeto, finObjeto + 1);
            Ticket ticketParseado = Ticket.fromJSONBloque(bloqueJson);
            if (ticketParseado != null) {
                listaResultado.agregar(ticketParseado);
            }
            posicionBusqueda = finObjeto + 1;
        }

        return listaResultado;
    }

    public void agregarTicket(Ticket ticket) {
        if (ticket == null) return;
        cache.agregar(ticket);
        guardarListaCompleta();
    }

    public void actualizarTicket(Ticket actualizado) {
        if (actualizado == null) return;

        int indice = 0;
        while (indice < cache.tamano()) {
            Ticket ticketActual = cache.obtener(indice);
            if (ticketActual != null) {
                if (ticketActual.getId() == actualizado.getId()
                    && mismoTexto(ticketActual.getHoraCompra(), actualizado.getHoraCompra())) {
                    cache.reemplazar(indice, actualizado);
                    guardarListaCompleta();
                    return;
                }
            }
            indice++;
        }

        cache.agregar(actualizado);
        guardarListaCompleta();
    }

    public boolean guardarListaCompleta() {
        String jsonTexto = "{\n  \"tickets\":[\n";
        int indice = 0;
        while (indice < cache.tamano()) {
            Ticket ticketActual = cache.obtener(indice);
            if (ticketActual != null) {
                jsonTexto += "    " + ticketActual.toJSON();
                if (indice < cache.tamano() - 1) jsonTexto += ",";
                jsonTexto += "\n";
            }
            indice++;
        }
        jsonTexto += "  ]\n}\n";
        return JsonUtilSimple.escribirArchivo(ruta, jsonTexto);
    }

    private boolean mismoTexto(String textoA, String textoB) {
        if (textoA == null && textoB == null) return true;
        if (textoA == null) return false;
        return textoA.equals(textoB);
    }
}