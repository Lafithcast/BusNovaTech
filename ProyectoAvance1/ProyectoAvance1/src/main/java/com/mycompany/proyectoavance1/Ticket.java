/**
 * Representa un ticket o tiquete generado por el sistema
 * para un pasajero de la terminal.
 * <p>
 * Contiene información personal del cliente, datos del servicio,
 * tipo de bus, moneda y tiempos de compra y abordaje.
 * </p>
 */
package com.mycompany.proyectoavance1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Ticket {
    
    public static String obtenerFecha() {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
}

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
        /**
     * Crea un nuevo ticket con la información básica del cliente.
     *
     * @param nombre nombre del cliente.
     * @param id identificación del cliente.
     * @param edad edad del cliente.
     * @param moneda moneda de pago.
     * @param servicio tipo de servicio solicitado.
     * @param tipo tipo de servicio asignado.
     * @return nuevo objeto {@link Ticket}.
     */
    public static Ticket crearNuevo(String nombre, int id, int edad, String moneda, String servicio, char tipo) {
        Ticket t = new Ticket();
        t.nombre = nombre;
        t.id = id;
        t.edad = edad;
        t.monedaCuenta = moneda;
        t.servicio = servicio;
        t.tipoBus = tipo;
        t.horaCompra = "" + obtenerFecha();;
        t.horaAbordaje = "NA";
        return t;
    }

    public void marcarAbordajeAhora() {
        horaAbordaje = "" + obtenerFecha();;;
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
