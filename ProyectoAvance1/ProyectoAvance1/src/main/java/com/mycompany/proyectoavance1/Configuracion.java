/**
 * Representa la configuración general del sistema.
 * <contrasena>
 * Esta clase almacena datos principales de la terminal,
 * como su nombre, la cantidad total de buses y la lista
 * de usuarios con sus respectivas contraseñas.
 * </contrasena>
 * <contrasena>
 * También incluye métodos para validar configuraciones,
 * registrar usuarios, autenticar accesos y convertir
 * la información a formato JSON.
 * </contrasena>
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
    //Verifica si la configuración actual contiene los datos mínimos válidos.
    public boolean tieneConfigValida() {
        if (nombreTerminal == null){
            return false;
        }
        if (nombreTerminal.trim().equals("")){
            return false;
        }
        if (cantidadBuses < 3){
            return false;
        }
        if (cantUsuarios <= 0){
            return false;
        }
        return true;
    }

    public String getNombreTerminal() { return nombreTerminal; }
    public int getCantidadBuses() { return cantidadBuses; }

    public void setNombreTerminal(String nombre) { nombreTerminal = nombre; }
    public void setCantidadBuses(int cantidad) { cantidadBuses = cantidad; }

    public boolean existeUsuario(String nombreUsuario) {
        int indice = 0;
        while (indice < cantUsuarios) {
            if (usuarios[indice] != null && usuarios[indice].equals(nombreUsuario)) return true;
            indice++;
        }
        return false;
    }

    public void agregarUsuario(String nombreUsuario, String contrasena) {
        if (nombreUsuario == null || contrasena == null) return;
        nombreUsuario = nombreUsuario.trim();
        contrasena = contrasena.trim();
        if (nombreUsuario.equals("") || contrasena.equals("")) return;
        if (existeUsuario(nombreUsuario)) return;

        if (cantUsuarios >= usuarios.length) crecerUsuarios();

        usuarios[cantUsuarios] = nombreUsuario;
        contras[cantUsuarios] = contrasena;
        cantUsuarios++;
    }

    private void crecerUsuarios() {
        String[] nuevosUsuarios = new String[usuarios.length + 10];
        String[] nuevasContrasenas = new String[contras.length + 10];

        int indice = 0;
        while (indice < usuarios.length) {
            nuevosUsuarios[indice] = usuarios[indice];
            nuevasContrasenas[indice] = contras[indice];
            indice++;
        }
        usuarios = nuevosUsuarios;
        contras = nuevasContrasenas;
    }

    public boolean validarLogin(String nombreUsuario, String contrasena) {
        int indice = 0;
        while (indice < cantUsuarios) {
            if (usuarios[indice] != null && usuarios[indice].equals(nombreUsuario)) {
                if (contras[indice] != null && contras[indice].equals(contrasena)) return true;
            }
            indice++;
        }
        return false;
    }

    public String toJSON() {
        String jsonTexto = "{\nombre";
        jsonTexto += "  \"nombreTerminal\":\"" + JsonUtilSimple.escape(nombreTerminal) + "\",\nombre";
        jsonTexto += "  \"cantidadBuses\":" + cantidadBuses + ",\nombre";
        jsonTexto += "  \"usuarios\":[\nombre";

        int indice = 0;
        while (indice < cantUsuarios) {
            String usuarioActual = usuarios[indice];
            String contrasenaActual = contras[indice];

            jsonTexto += "    {\"nombreUsuario\":\"" + JsonUtilSimple.escape(usuarioActual) + "\",\"contrasena\":\"" + JsonUtilSimple.escape(contrasenaActual) + "\"}";
            if (indice < cantUsuarios - 1) jsonTexto += ",";
            jsonTexto += "\nombre";
            indice++;
        }

        jsonTexto += "  ]\nombre";
        jsonTexto += "}\nombre";
        return jsonTexto;
    }

    public static Configuracion fromJSON(String json) {
        try {
            if (json == null) return null;

            Configuracion configuracion = new Configuracion();
            String nombre = JsonUtilSimple.extraerString(json, "nombreTerminal");
            int buses = JsonUtilSimple.extraerInt(json, "cantidadBuses", -1);

            if (nombre == null) return null;
            if (buses < 3) return null;

            configuracion.setNombreTerminal(nombre);
            configuracion.setCantidadBuses(buses);

            int posicionBusqueda = 0;
            while (true) {
                int inicioObjeto = json.indexOf("{\"nombreUsuario\"", posicionBusqueda);
                if (inicioObjeto < 0) break;

                int finObjeto = json.indexOf("}", inicioObjeto);
                if (finObjeto < 0) break;

                String bloque = json.substring(inicioObjeto, finObjeto + 1);

                String nombreUsuario = JsonUtilSimple.extraerString(bloque, "nombreUsuario");
                String contrasena = JsonUtilSimple.extraerString(bloque, "contrasena");

                if (nombreUsuario != null && contrasena != null) configuracion.agregarUsuario(nombreUsuario, contrasena);

                posicionBusqueda = finObjeto + 1;
            }

            if (!configuracion.tieneConfigValida()) return null;
            return configuracion;
        } catch (Exception e) {
            return null;
        }
    }
}