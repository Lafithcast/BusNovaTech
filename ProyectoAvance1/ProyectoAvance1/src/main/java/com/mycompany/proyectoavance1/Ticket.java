//Authors = Allison Alvarado, Lafith Castrillo, Fernanda Herrera & Nicolas Peñas
package com.mycompany.proyectoavance1;
public class Ticket {

    private String nombre;
    private int id;
    private int edad;
    private String monedaCuenta;
    private String horaCompra;
    private String horaAbordaje;
    private String servicio;
    private char tipoBus;

    public Ticket() {
        horaAbordaje = "NA";
    }

    public static Ticket crearNuevo(String nombre, int id, int edad, String moneda, String servicio, char tipo) {
        Ticket t = new Ticket();
        t.nombre = nombre;
        t.id = id;
        t.edad = edad;
        t.monedaCuenta = moneda;
        t.servicio = servicio;
        t.tipoBus = tipo;
        t.horaCompra = "" + System.currentTimeMillis();
        t.horaAbordaje = "NA";
        return t;
    }

    public void marcarAbordajeAhora() {
        horaAbordaje = "" + System.currentTimeMillis();
    }

    public String resumen() {
        return "Nombre=" + nombre +
                " | ID=" + id +
                " | Edad=" + edad +
                " | Moneda=" + monedaCuenta +
                " | Compra=" + horaCompra +
                " | Abordaje=" + horaAbordaje +
                " | Servicio=" + servicio +
                " | TipoBus=" + tipoBus;
    }

    public int getId() { return id; }
    public String getHoraCompra() { return horaCompra; }
    public String getHoraAbordaje() { return horaAbordaje; }
    public char getTipoBus() { return tipoBus; }

    public String toJSON() {
        String j = "{";
        j += "\"nombre\":\"" + JsonUtilSimple.escape(nombre) + "\",";
        j += "\"id\":" + id + ",";
        j += "\"edad\":" + edad + ",";
        j += "\"moneda\":\"" + JsonUtilSimple.escape(monedaCuenta) + "\",";
        j += "\"horaCompra\":\"" + JsonUtilSimple.escape(horaCompra) + "\",";
        j += "\"horaAbordaje\":\"" + JsonUtilSimple.escape(horaAbordaje) + "\",";
        j += "\"servicio\":\"" + JsonUtilSimple.escape(servicio) + "\",";
        j += "\"tipoBus\":\"" + tipoBus + "\"";
        j += "}";
        return j;
    }

    public static Ticket fromJSONBloque(String bloque) {
        try {
            if (bloque == null) return null;

            String nombre = JsonUtilSimple.extraerString(bloque, "nombre");
            int id = JsonUtilSimple.extraerInt(bloque, "id", -1);
            int edad = JsonUtilSimple.extraerInt(bloque, "edad", -1);
            String moneda = JsonUtilSimple.extraerString(bloque, "moneda");
            String horaCompra = JsonUtilSimple.extraerString(bloque, "horaCompra");
            String horaAbordaje = JsonUtilSimple.extraerString(bloque, "horaAbordaje");
            String servicio = JsonUtilSimple.extraerString(bloque, "servicio");
            String tipo = JsonUtilSimple.extraerString(bloque, "tipoBus");

            if (nombre == null) return null;
            if (id < 1) return null;
            if (edad < 0) return null;
            if (moneda == null || horaCompra == null || horaAbordaje == null || servicio == null || tipo == null) return null;
            if (tipo.length() < 1) return null;

            Ticket t = new Ticket();
            t.nombre = nombre;
            t.id = id;
            t.edad = edad;
            t.monedaCuenta = moneda;
            t.horaCompra = horaCompra;
            t.horaAbordaje = horaAbordaje;
            t.servicio = servicio;
            t.tipoBus = tipo.charAt(0);

            return t;
        } catch (Exception e) {
            return null;
        }
    }
}
