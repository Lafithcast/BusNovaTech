//Authors = Allison Alvarado, Lafith Castrillo, Fernanda Herrera & Nicolas Peñas
package com.mycompany.proyectoavance1;
public class ConfigRepository {

    private String ruta;

    public ConfigRepository(String rutaArchivo) {
        ruta = rutaArchivo;
    }

    public Configuracion cargar() {
        String json = JsonUtilSimple.leerArchivo(ruta);
        if (json == null) return null;
            return Configuracion.fromJSON(json);
    }

    public boolean guardar(Configuracion c) {
        if (c == null) return false;
            return JsonUtilSimple.escribirArchivo(ruta, c.toJSON());
    }
}
