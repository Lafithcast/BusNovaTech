/**
 * Clase utilitaria para trabajar con archivos y cadenas JSON
 * de manera sencilla, sin utilizar librerías externas.
 * <p>
 * Incluye métodos para leer y escribir archivos de texto,
 * escapar caracteres especiales y extraer valores básicos
 * desde una cadena en formato JSON.
 * </p>
 */
package com.mycompany.proyectoavance1;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class JsonUtilSimple {

    public static String leerArchivo(String ruta) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(ruta));
            String lineaActual;
            String contenido = "";
            while ((lineaActual = br.readLine()) != null) {
                contenido = contenido + lineaActual + "\n";
            }
            br.close();
            return contenido;
        } catch (Exception e) {
            try { if (br != null) br.close(); } catch (Exception ex) { }
            return null;
        }
    }

    public static boolean escribirArchivo(String ruta, String contenido) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(ruta);
            fw.write(contenido);
            fw.close();
            return true;
        } catch (Exception e) {
            try { if (fw != null) fw.close(); } catch (Exception ex) { }
            return false;
        }
    }

    public static String escape(String texto) {
        if (texto == null) return "";
        texto = texto.replace("\\", "\\\\");
        texto = texto.replace("\"", "\\\"");
        return texto;
    }

    public static String unescape(String texto) {
        if (texto == null) return "";
        texto = texto.replace("\\\"", "\"");
        texto = texto.replace("\\\\", "\\");
        return texto;
    }

    public static String extraerString(String json, String key) {
        try {
            String patron = "\"" + key + "\"";
            int posicionClave = json.indexOf(patron);
            if (posicionClave < 0) return null;

            int dosP = json.indexOf(":", posicionClave);
            if (dosP < 0) return null;

            int q1 = json.indexOf("\"", dosP);
            if (q1 < 0) return null;

            int q2 = json.indexOf("\"", q1 + 1);
            while (q2 >= 0 && json.charAt(q2 - 1) == '\\') {
                q2 = json.indexOf("\"", q2 + 1);
            }
            if (q2 < 0) return null;

            String raw = json.substring(q1 + 1, q2);
            return unescape(raw);
        } catch (Exception e) {
            return null;
        }
    }

    public static int extraerInt(String json, String key, int fallback) {
        try {
            String patron = "\"" + key + "\"";
            int posicionClave = json.indexOf(patron);
            if (posicionClave < 0) return fallback;

            int dosP = json.indexOf(":", posicionClave);
            if (dosP < 0) return fallback;

            int inicioNumero = dosP + 1;
            while (inicioNumero < json.length() && (json.charAt(inicioNumero) == ' ' || json.charAt(inicioNumero) == '\n' || json.charAt(inicioNumero) == '\r' || json.charAt(inicioNumero) == '\t')) inicioNumero++;

            int finNumero = inicioNumero;
            while (finNumero < json.length()) {
                char c = json.charAt(finNumero);
                if ((c >= '0' && c <= '9') || c == '-') finNumero++;
                else break;
            }
            String num = json.substring(inicioNumero, finNumero).trim();
            return Integer.parseInt(num);
        } catch (Exception e) {
            return fallback;
        }
    }
}