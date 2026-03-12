//Authors = Allison Alvarado, Lafith Castrillo, Fernanda Herrera & Nicolas Peñas
package com.mycompany.proyectoavance1;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class JsonUtilSimple {

    public static String leerArchivo(String ruta) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(ruta));
            String line;
            String contenido = "";
            while ((line = br.readLine()) != null) {
                contenido = contenido + line + "\n";
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

    public static String escape(String s) {
        if (s == null) return "";
        s = s.replace("\\", "\\\\");
        s = s.replace("\"", "\\\"");
        return s;
    }

    public static String unescape(String s) {
        if (s == null) return "";
        s = s.replace("\\\"", "\"");
        s = s.replace("\\\\", "\\");
        return s;
    }

    public static String extraerString(String json, String key) {
        try {
            String patron = "\"" + key + "\"";
            int i = json.indexOf(patron);
            if (i < 0) return null;

            int dosP = json.indexOf(":", i);
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
            int i = json.indexOf(patron);
            if (i < 0) return fallback;

            int dosP = json.indexOf(":", i);
            if (dosP < 0) return fallback;

            int j = dosP + 1;
            while (j < json.length() && (json.charAt(j) == ' ' || json.charAt(j) == '\n' || json.charAt(j) == '\r' || json.charAt(j) == '\t')) j++;

            int k = j;
            while (k < json.length()) {
                char c = json.charAt(k);
                if ((c >= '0' && c <= '9') || c == '-') k++;
                else break;
            }
            String num = json.substring(j, k).trim();
            return Integer.parseInt(num);
        } catch (Exception e) {
            return fallback;
        }
    }
}