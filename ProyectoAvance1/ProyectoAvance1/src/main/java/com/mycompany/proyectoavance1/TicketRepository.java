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
        int i = 0;
        while (i < cargados.length) {
            if (cargados[i] != null) agregarEnCache(cargados[i]);
            i++;
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
    }

    public void agregarTicket(Ticket t) {
        if (t == null) return;
        agregarEnCache(t);
        guardarListaCompleta();
    }

    public void actualizarTicket(Ticket actualizado) {
        if (actualizado == null) return;

        int i = 0;
        while (i < cant) {
            Ticket t = cache[i];
            if (t != null) {
                if (t.getId() == actualizado.getId() && mismoTexto(t.getHoraCompra(), actualizado.getHoraCompra())) {
                    cache[i] = actualizado;
                    guardarListaCompleta();
                    return;
                }
            }
            i++;
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
        Ticket[] n = new Ticket[cache.length + 200];
        int i = 0;
        while (i < cache.length) {
            n[i] = cache[i];
            i++;
        }
        cache = n;
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
