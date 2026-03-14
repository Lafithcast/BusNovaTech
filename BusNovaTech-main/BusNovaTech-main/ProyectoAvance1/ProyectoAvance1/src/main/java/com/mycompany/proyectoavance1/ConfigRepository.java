/**
 * Repositorio encargado de administrar la carga y el guardado
 * de la configuración general del sistema en un archivo JSON.
 * <p>
 * Esta clase permite leer la información almacenada en
 * <code>config.json</code> y convertirla en un objeto
 * {@link Configuracion}, así como guardar nuevamente los cambios
 * realizados en la configuración.
 * </p>
 */
package com.mycompany.proyectoavance1;
public class ConfigRepository {

    private String ruta;

    public ConfigRepository(String rutaArchivo) {
        ruta = rutaArchivo;
    }
    //Carga la configuración desde el archivo JSON
    public Configuracion cargar() {
        String json = JsonUtilSimple.leerArchivo(ruta);
        if (json == null) return null;
            return Configuracion.fromJSON(json);
    }
    //Guarda la configuración recibida en el archivo JSON
    public boolean guardar(Configuracion c) {
        if (c == null) return false;
            return JsonUtilSimple.escribirArchivo(ruta, c.toJSON());
    }
}
