//Authors = Allison Alvarado, Lafith Castrillo, Fernanda Herrera & Nicolas Peñas
package com.mycompany.proyectoavance1;
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
        if (json == null) return new Ticket[0];

        Ticket[] temp = new Ticket[500];
        int idx = 0;

        int pos = 0;
        while (true) {
            int obj = json.indexOf("{\"nombre\"", pos);
            if (obj < 0) break;

            int fin = json.indexOf("}", obj);
            if (fin < 0) break;

            String bloque = json.substring(obj, fin + 1);
            Ticket t = Ticket.fromJSONBloque(bloque);
            if (t != null) {
                if (idx < temp.length) {
                    temp[idx] = t;
                    idx++;
                }
            }
            pos = fin + 1;
        }

        Ticket[] rec = new Ticket[idx];
        int i = 0;
        while (i < idx) { rec[i] = temp[i]; i++; }
        return rec;
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
        String j = "{\n  \"tickets\":[\n";
        int i = 0;
        while (i < cant) {
            if (cache[i] != null) {
                j += "    " + cache[i].toJSON();
                if (i < cant - 1) j += ",";
                j += "\n";
            }
            i++;
        }
        j += "  ]\n}\n";
        return JsonUtilSimple.escribirArchivo(ruta, j);
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
