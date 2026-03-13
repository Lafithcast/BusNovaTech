/**
 * Representa la configuración general del sistema.
 * <p>
 * Esta clase almacena datos principales de la terminal,
 * como su nombre, la cantidad total de buses y la lista
 * de usuarios con sus respectivas contraseñas.
 * </p>
 * <p>
 * También incluye métodos para validar configuraciones,
 * registrar usuarios, autenticar accesos y convertir
 * la información a formato JSON.
 * </p>
 */


package com.mycompany.proyectoavance1;
public class Configuracion {

    private String nombreTerminal;
    private int cantidadBuses;

    private String[] usuarios;
    private String[] contras;
    private int cantUsuarios;

    public Configuracion() {
        nombreTerminal = "";
        cantidadBuses = 0;
        usuarios = new String[10];
        contras = new String[10];
        cantUsuarios = 0;
    }
    // Verifica si la configuración actual contiene los datos mínimos válidos.
    public boolean tieneConfigValida() {
        if (nombreTerminal == null) return false;
        if (nombreTerminal.trim().equals("")) return false;
        if (cantidadBuses < 3) return false;
        if (cantUsuarios <= 0) return false;
        return true;
    }

    public String getNombreTerminal() { return nombreTerminal; }
    public int getCantidadBuses() { return cantidadBuses; }

    public void setNombreTerminal(String n) { nombreTerminal = n; }
    public void setCantidadBuses(int c) { cantidadBuses = c; }

    public boolean existeUsuario(String u) {
        int i = 0;
        while (i < cantUsuarios) {
            if (usuarios[i] != null && usuarios[i].equals(u)) return true;
            i++;
        }
        return false;
    }

    public void agregarUsuario(String u, String p) {
        if (u == null || p == null) return;
        u = u.trim();
        p = p.trim();
        if (u.equals("") || p.equals("")) return;
        if (existeUsuario(u)) return;

        if (cantUsuarios >= usuarios.length) crecerUsuarios();

        usuarios[cantUsuarios] = u;
        contras[cantUsuarios] = p;
        cantUsuarios++;
    }

    private void crecerUsuarios() {
        String[] nu = new String[usuarios.length + 10];
        String[] np = new String[contras.length + 10];

        int i = 0;
        while (i < usuarios.length) {
            nu[i] = usuarios[i];
            np[i] = contras[i];
            i++;
        }
        usuarios = nu;
        contras = np;
    }

    public boolean validarLogin(String u, String p) {
        int i = 0;
        while (i < cantUsuarios) {
            if (usuarios[i] != null && usuarios[i].equals(u)) {
                if (contras[i] != null && contras[i].equals(p)) return true;
            }
            i++;
        }
        return false;
    }

    public String toJSON() {
        String j = "{\n";
        j += "  \"nombreTerminal\":\"" + JsonUtilSimple.escape(nombreTerminal) + "\",\n";
        j += "  \"cantidadBuses\":" + cantidadBuses + ",\n";
        j += "  \"usuarios\":[\n";

        int i = 0;
        while (i < cantUsuarios) {
            String u = usuarios[i];
            String p = contras[i];

            j += "    {\"u\":\"" + JsonUtilSimple.escape(u) + "\",\"p\":\"" + JsonUtilSimple.escape(p) + "\"}";
            if (i < cantUsuarios - 1) j += ",";
            j += "\n";
            i++;
        }

        j += "  ]\n";
        j += "}\n";
        return j;
    }

    public static Configuracion fromJSON(String json) {
        try {
            if (json == null) return null;

            Configuracion c = new Configuracion();
            String nombre = JsonUtilSimple.extraerString(json, "nombreTerminal");
            int buses = JsonUtilSimple.extraerInt(json, "cantidadBuses", -1);

            if (nombre == null) return null;
            if (buses < 3) return null;

            c.setNombreTerminal(nombre);
            c.setCantidadBuses(buses);

            int pos = 0;
            while (true) {
                int obj = json.indexOf("{\"u\"", pos);
                if (obj < 0) break;

                int fin = json.indexOf("}", obj);
                if (fin < 0) break;

                String bloque = json.substring(obj, fin + 1);

                String u = JsonUtilSimple.extraerString(bloque, "u");
                String p = JsonUtilSimple.extraerString(bloque, "p");

                if (u != null && p != null) c.agregarUsuario(u, p);

                pos = fin + 1;
            }

            if (!c.tieneConfigValida()) return null;
            return c;
        } catch (Exception e) {
            return null;
        }
    }
}