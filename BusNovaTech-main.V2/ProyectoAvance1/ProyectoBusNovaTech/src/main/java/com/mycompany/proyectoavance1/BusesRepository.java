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
public class BusesRepository {
    private String ruta;
    private ListaBuses cacheBuses;

    public BusesRepository(String rutaArchivo) {
        ruta = rutaArchivo;
        cacheBuses = cargarBuses();
    }

    public ListaBuses cargarBuses() {
        try {
            Gson gson = new Gson();
            FileReader lectorArchivo = new FileReader(ruta);
            ListaBuses listaLeida = gson.fromJson(lectorArchivo, ListaBuses.class);
            lectorArchivo.close();

            if (listaLeida != null) {
                return listaLeida;
            }
        } catch (Exception e) {
        }

        return new ListaBuses();
    }

    public ListaBuses obtenerBuses() {
        return cacheBuses;
    }

    public void setCacheBuses(ListaBuses listaBuses) {
        if (listaBuses != null) {
            cacheBuses = listaBuses;
        }
    }

    public boolean guardarBuses(ListaBuses listaBuses) {
        if (listaBuses != null) {
            cacheBuses = listaBuses;
        }

        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter escritorArchivo = new FileWriter(ruta);
            gson.toJson(cacheBuses, escritorArchivo);
            escritorArchivo.flush();
            escritorArchivo.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
