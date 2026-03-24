/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoavance1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
/**
 *
 * @author lcast
 */
public class AtendidosRepository {
    private String ruta;
    private ListaTickets cacheAtendidos;

    public AtendidosRepository(String rutaArchivo) {
        ruta = rutaArchivo;
        cacheAtendidos = cargarAtendidos();
    }

    public ListaTickets cargarAtendidos() {
        try {
            Gson gson = new Gson();
            FileReader lectorArchivo = new FileReader(ruta);
            ListaTickets listaLeida = gson.fromJson(lectorArchivo, ListaTickets.class);
            lectorArchivo.close();

            if (listaLeida != null) {
                return listaLeida;
            }
        } catch (Exception exception) {
        }

        return new ListaTickets();
    }

    public void agregarAtendido(Ticket ticketAtendido) {
        if (ticketAtendido == null) {
            return;
        }

        cacheAtendidos.agregar(ticketAtendido);
        guardarListaCompleta();
    }

    public ListaTickets obtenerAtendidos() {
        return cacheAtendidos;
    }

    public boolean guardarListaCompleta() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter escritorArchivo = new FileWriter(ruta);
            gson.toJson(cacheAtendidos, escritorArchivo);
            escritorArchivo.flush();
            escritorArchivo.close();
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
