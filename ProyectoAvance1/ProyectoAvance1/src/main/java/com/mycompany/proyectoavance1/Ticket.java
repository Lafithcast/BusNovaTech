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
import com.google.gson.Gson;

public class Ticket {
    
    public static String obtenerFecha() {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
}

    public String nombre;
    public int id;
    public int edad;
    public String monedaCuenta;
    public String horaCompra;
    public String horaAbordaje;
    public String servicio; // Usado como tipoServicio
    public char tipoBus;
    // Nuevos campos
    public String estado;
    public String terminalCompra;
    public int busAsignado;
    public int librasCarga;

    public Ticket() {
        horaAbordaje = "NA";
        estado = "Pendiente";
        terminalCompra = "";
        busAsignado = 0;
        librasCarga = 0;
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
        ticket.nombre = nombre;
        ticket.id = id;
        ticket.edad = edad;
        ticket.monedaCuenta = moneda;
        ticket.servicio = servicio;
        ticket.tipoBus = tipo;
        ticket.horaCompra = "" + obtenerFecha();
        ticket.horaAbordaje = "NA";
        ticket.estado = "Pendiente";
        ticket.librasCarga = 0; 
        return t;
    }

    public void marcarAbordajeAhora() {
        horaAbordaje = "" + obtenerFecha();
    }

    public String resumen() {
        return "Nombre=" + nombre +
                " | ID=" + id +
                " | Edad=" + edad +
                " | Moneda=" + monedaCuenta +
                " | Compra=" + horaCompra +
                " | Atencion=" + horaAbordaje +
                " | Servicio=" + servicio +
                " | TipoBus=" + tipoBus +
                " | Estado=" + estado +
                " | Terminal=" + terminalCompra +
                " | Bus=" + busAsignado +
                " | Libras=" + librasCarga;
    }

    public int getId() { return id; }
    public String getHoraCompra() { return horaCompra; }
    public String getHoraAbordaje() { return horaAbordaje; }
    public char getTipoBus() { return tipoBus; }
    public String getEstado() { return estado; }
    public String getTerminalCompra() { return terminalCompra; }
    public int getBusAsignado() { return busAsignado; }
    public int getLibrasCarga() { return librasCarga; }
    public void setEstado(String estado) { estado = estado; }
    public void setTerminalCompra(String t) { terminalCompra = t; }
    public void setBusAsignado(int b) { busAsignado = b; }
    public void setLibrasCarga(int l) { librasCarga = l; }

    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Ticket fromJSONBloque(String bloque) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(bloque, Ticket.class);
        } catch (Exception e) {
            return null;
        }
    }
}
