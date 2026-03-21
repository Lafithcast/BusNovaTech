/**
 * Clase utilitaria para capturar datos mediante cuadros de diálogo.
 * <p>
 * Proporciona métodos para leer textos, números enteros y opciones
 * válidas, aplicando validaciones básicas para evitar entradas vacías
 * o incorrectas.
 * </p>
 */
package com.mycompany.proyectoavance1;
public class InputJOP {

    public String leerTextoNoVacio(String mensaje) {
        while (true) {
            String textoIngresado = javax.swing.JOptionPane.showInputDialog(mensaje);
            if (textoIngresado == null) {
                javax.swing.JOptionPane.showMessageDialog(null, "intenta de nuevo.");
                continue;
            }
            textoIngresado = textoIngresado.trim();
            if (!textoIngresado.equals("")) return textoIngresado;
                javax.swing.JOptionPane.showMessageDialog(null, "Entrada invalida.");
        }
    }

    public int leerEnteroRango(String mensaje, int min, int max) {
        while (true) {
            String textoIngresado = javax.swing.JOptionPane.showInputDialog(mensaje);
            if (textoIngresado == null) {
                javax.swing.JOptionPane.showMessageDialog(null, "Intenta de nuevo.");
                continue;
            }
            textoIngresado = textoIngresado.trim();

            int valorIngresado = parseEnteroSeguro(textoIngresado, Integer.MIN_VALUE);
            if (valorIngresado == Integer.MIN_VALUE) {
                javax.swing.JOptionPane.showMessageDialog(null, "numero invalido");
                continue;
            }

            if (valorIngresado < min || valorIngresado > max) {
                javax.swing.JOptionPane.showMessageDialog(null, "Fuera de rango (" + min + " a " + max + ").");
                continue;
            }
            return valorIngresado;
        }
    }

    public String leerOpcionTexto(String mensaje, String op1, String op2) {
        while (true) {
            String textoIngresado = javax.swing.JOptionPane.showInputDialog(mensaje);
            if (textoIngresado == null) {
                javax.swing.JOptionPane.showMessageDialog(null, "Intenta de nuevo.");
                continue;
            }
            textoIngresado = textoIngresado.trim();
            if (textoIngresado.equals(op1) || textoIngresado.equals(op2)) return textoIngresado;
                javax.swing.JOptionPane.showMessageDialog(null, "Intenta de nuevo.");
        }
    }

    public String leerOpcionTexto(String mensaje, String op1, String op2, String op3) {
        while (true) {
            String textoIngresado = javax.swing.JOptionPane.showInputDialog(mensaje);
            if (textoIngresado == null) {
                javax.swing.JOptionPane.showMessageDialog(null, "Intenta de nuevo.");
                continue;
            }
            textoIngresado = textoIngresado.trim();
            if (textoIngresado.equals(op1) || textoIngresado.equals(op2) || textoIngresado.equals(op3)) return textoIngresado;
                javax.swing.JOptionPane.showMessageDialog(null, "Intenta de nuevo.");
        }
    }

    public String leerOpcionTexto(String mensaje, String op1, String op2, String op3, String op4, String op5) {
        while (true) {
            String textoIngresado = javax.swing.JOptionPane.showInputDialog(mensaje);
            if (textoIngresado == null) {
                javax.swing.JOptionPane.showMessageDialog(null, "Intenta de nuevo.");
                continue;
            }
            textoIngresado = textoIngresado.trim();
            if (textoIngresado.equals(op1) || textoIngresado.equals(op2) || textoIngresado.equals(op3) || textoIngresado.equals(op4) || textoIngresado.equals(op5)) return textoIngresado;
                javax.swing.JOptionPane.showMessageDialog(null, "Intenta de nuevo.");
        }
    }

    public int parseEnteroSeguro(String textoIngresado, int fallback) {
        try {
            return Integer.parseInt(textoIngresado);
        } catch (Exception e) {
            return fallback;
        }
    }
}

