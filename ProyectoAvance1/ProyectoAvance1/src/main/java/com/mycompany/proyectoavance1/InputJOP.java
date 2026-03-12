//Authors = Allison Alvarado, Lafith Castrillo, Fernanda Herrera & Nicolas Peñas
package com.mycompany.proyectoavance1;
public class InputJOP {

    public String leerTextoNoVacio(String msg) {
        while (true) {
            String s = javax.swing.JOptionPane.showInputDialog(msg);
            if (s == null) {
                javax.swing.JOptionPane.showMessageDialog(null, "intenta de nuevo.");
                continue;
            }
            s = s.trim();
            if (!s.equals("")) return s;
                javax.swing.JOptionPane.showMessageDialog(null, "Entrada invalida.");
        }
    }

    public int leerEnteroRango(String msg, int min, int max) {
        while (true) {
            String s = javax.swing.JOptionPane.showInputDialog(msg);
            if (s == null) {
                javax.swing.JOptionPane.showMessageDialog(null, "Intenta de nuevo.");
                continue;
            }
            s = s.trim();

            int n = parseEnteroSeguro(s, Integer.MIN_VALUE);
            if (n == Integer.MIN_VALUE) {
                javax.swing.JOptionPane.showMessageDialog(null, "numero invalido");
                continue;
            }

            if (n < min || n > max) {
                javax.swing.JOptionPane.showMessageDialog(null, "Fuera de rango (" + min + " a " + max + ").");
                continue;
            }
            return n;
        }
    }

    public String leerOpcionTexto(String msg, String op1, String op2) {
        while (true) {
            String s = javax.swing.JOptionPane.showInputDialog(msg);
            if (s == null) {
                javax.swing.JOptionPane.showMessageDialog(null, "Intenta de nuevo.");
                continue;
            }
            s = s.trim();
            if (s.equals(op1) || s.equals(op2)) return s;
                javax.swing.JOptionPane.showMessageDialog(null, "Intenta de nuevo.");
        }
    }

    public String leerOpcionTexto(String msg, String op1, String op2, String op3) {
        while (true) {
            String s = javax.swing.JOptionPane.showInputDialog(msg);
            if (s == null) {
                javax.swing.JOptionPane.showMessageDialog(null, "Intenta de nuevo.");
                continue;
            }
            s = s.trim();
            if (s.equals(op1) || s.equals(op2) || s.equals(op3)) return s;
                javax.swing.JOptionPane.showMessageDialog(null, "Intenta de nuevo.");
        }
    }

    public String leerOpcionTexto(String msg, String op1, String op2, String op3, String op4, String op5) {
        while (true) {
            String s = javax.swing.JOptionPane.showInputDialog(msg);
            if (s == null) {
                javax.swing.JOptionPane.showMessageDialog(null, "Intenta de nuevo.");
                continue;
            }
            s = s.trim();
            if (s.equals(op1) || s.equals(op2) || s.equals(op3) || s.equals(op4) || s.equals(op5)) return s;
                javax.swing.JOptionPane.showMessageDialog(null, "Intenta de nuevo.");
        }
    }

    public int parseEnteroSeguro(String s, int fallback) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return fallback;
        }
    }
}

