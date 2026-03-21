/**
 * Repositorio encargado de la administración de tickets
 * almacenados en un archivo JSON.
 * <p>
 * Permite cargar, guardar, agregar y actualizar tickets
 * dentro del archivo de persistencia del sistema.
 * </p>
 */
package com.mycompany.proyectoavance1;
import com.google.gson.Gson;
import java.util.Arrays;
public class TicketRepository {

    private String ruta;
    private Ticket[] cache;
    private int cant;

    public TicketRepository(String rutaArchivo) {
        ruta = rutaArchivo;
        cache = new Ticket[200];
        cant = 0;

        Ticket[] cargados = cargarTickets();
        int indice = 0;
        while (indice < cargados.length) {
            if (cargados[indice] != null) agregarEnCache(cargados[indice]);
            indice++;
        }
    }

    public Ticket[] cargarTickets() {
        String json = JsonUtilSimple.leerArchivo(ruta);
        if (json == null || json.trim().isEmpty()) return new Ticket[0];
        try {
            Gson gson = new Gson();
            TicketsWrapper tw = gson.fromJson(json, TicketsWrapper.class);
            return tw != null && tw.tickets != null ? tw.tickets : new Ticket[0];
        } catch (Exception e) {
            return new Ticket[0];
        }
        if (json == null) return new Ticket[0];

        Ticket[] ticketTemporales = new Ticket[500];
        int indiceActual = 0;

        int posicionBusqueda = 0;
        while (true) {
            int inicioObjeto = json.indexOf("{\"nombre\"", posicionBusqueda);
            if (inicioObjeto < 0) break;

            int finObjeto = json.indexOf("}", inicioObjeto);
            if (finObjeto < 0) break;

            String bloqueJson = json.substring(inicioObjeto, finObjeto + 1);
            Ticket t = Ticket.fromJSONBloque(bloqueJson);
            if (t != null) {
                if (indiceActual < ticketTemporales.length) {
                    ticketTemporales[indiceActual] = t;
                    indiceActual++;
                }
            }
            posicionBusqueda = finObjeto + 1;
        }

        Ticket[] ticketRecuperados = new Ticket[indiceActual];
        int indice = 0;
        while (indice < indiceActual) { ticketRecuperados[indice] = ticketTemporales[indice]; indice++; }
        return ticketRecuperados;
    }

    public void agregarTicket(Ticket t) {
        if (t == null) return;
        agregarEnCache(t);
        guardarListaCompleta();
    }

    public void actualizarTicket(Ticket actualizado) {
        if (actualizado == null) return;

        int indice = 0;
        while (indice < cant) {
            Ticket t = cache[indice];
            if (t != null) {
                if (t.getId() == actualizado.getId() && mismoTexto(t.getHoraCompra(), actualizado.getHoraCompra())) {
                    cache[indice] = actualizado;
                    guardarListaCompleta();
                    return;
                }
            }
            indice++;
        }

        agregarEnCache(actualizado);
        guardarListaCompleta();
    }

    public boolean guardarListaCompleta() {
        Gson gson = new Gson();
        TicketsWrapper tw = new TicketsWrapper();
        tw.tickets = Arrays.copyOf(cache, cant);
        String json = gson.toJson(tw);
        return JsonUtilSimple.escribirArchivo(ruta, json);
    }

    private void agregarEnCache(Ticket t) {
        if (cant >= cache.length) crecer();
        cache[cant] = t;
        cant++;
    }

    private void crecer() {
        Ticket[] nuevoCache = new Ticket[cache.length + 200];
        int indice = 0;
        while (indice < cache.length) {
            nuevoCache[indice] = cache[indice];
            indice++;
        }
        cache = nuevoCache;
    }

    private boolean mismoTexto(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null) return false;
        return a.equals(b);
    }
}

class TicketsWrapper {
    public Ticket[] tickets;
}
