//Authors = Allison Alvarado, Lafith Castrillo, Fernanda Herrera & Nicolas Peñas
package com.mycompany.proyectoavance1;
public class Proyecto {

    private Configuracion config;
    private ColaPrioridad cola;
    private InputJOP in;
    private Persistence persistence;

    public Proyecto() {
        cola = new ColaPrioridad();
        in = new InputJOP();
        persistence = new Persistence();
        config = null;
    }

    public void iniciar() {
        javax.swing.JOptionPane.showMessageDialog(null, "Bienvenido al Sistema");

        //config.json
        config = persistence.getConfigRepository().cargar();
        if (config == null) {
            javax.swing.JOptionPane.showMessageDialog(null, "config.json no existe o esta dañado.");
            config = new Configuracion();
            configurarDesdeCero();
            persistence.getConfigRepository().guardar(config);
        }

        //tiquetes.json
        Ticket[] guardados = persistence.getTicketRepository().cargarTickets();
        int i = 0;
        while (i < guardados.length) {
            if (guardados[i] != null) cola.encolar(guardados[i]);
            i++;
        }

        if (!login()) {
            javax.swing.JOptionPane.showMessageDialog(null, "Demasiados intentos. saliendo.");
            return;
        }

        menu();
    }

    private void configurarDesdeCero() {
        String nombreTerminal = in.leerTextoNoVacio("Nombre de la terminal:");
        int totalBuses = in.leerEnteroRango("Cantidad total de buses :\nExcluyendo 1 normal y 1 preferencial.",
                3, 200
        );

        config.setNombreTerminal(nombreTerminal);
        config.setCantidadBuses(totalBuses);

        javax.swing.JOptionPane.showMessageDialog(null, "Crea un usuario inicial");
        String u = in.leerTextoNoVacio("Usuario:");
        String p = in.leerTextoNoVacio("Contraseña:");
        config.agregarUsuario(u, p);
    }

    private boolean login() {
        int intentos = 0;
        while (intentos < 3) {
            String u = in.leerTextoNoVacio("LOGIN\nUsuario:");
            String p = in.leerTextoNoVacio("LOGIN\nContraseña:");

            if (config.validarLogin(u, p)) {
                javax.swing.JOptionPane.showMessageDialog(null, "Bienvenido a " + config.getNombreTerminal());
                return true;
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectas. Intente nuevamente.");
            }
            intentos++;
        }
        return false;
    }

    private void menu() {
        while (true) {
            String opStr = javax.swing.JOptionPane.showInputDialog(
                    "|MENÚ |\n" +
                    "1) Crear ticket\n" +
                    "2) Llamar siguiente\n" +
                    "3) Ver estado de colas\n" +
                    "4) Agregar usuario\n" +
                    "0) Salir\n\n" +
                    "Opcion:"
            );

            if (opStr == null) {
                salirGuardando();
                return;
            }

            int op = in.parseEnteroSeguro(opStr, -1);

            if (op == 1) crearTicket();
            else if (op == 2) llamarSiguiente();
            else if (op == 3) verEstado();
            else if (op == 4) agregarUsuario();
            else if (op == 0) {
                salirGuardando();
                return;
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Intenta de nuevo.");
            }
        }
    }

    private void crearTicket() {
        String nombre = in.leerTextoNoVacio("Crear Ticket\nNombre:");
        int id = in.leerEnteroRango("Crear Ticket\nID (1..999999999):", 1, 999999999);
        int edad = in.leerEnteroRango("Crear Ticket\nEdad (0..120):", 0, 120);

        String monedaOp = in.leerOpcionTexto(
                "Crear Ticket\nMoneda/Cuenta:\n1) COLONES\n2) DOLARES",
                "1", "2"
        );
        String moneda = monedaOp.equals("1") ? "COLONES" : "DOLARES";

        String servicioOp = in.leerOpcionTexto(
                "Crear Ticket\nServicio:\n1) VIP\n2) REGULAR\n3) CARGA\n4) EJECUTIVO\n5) NA",
                "1","2","3","4","5"
        );
        String servicio = "NA";
        if (servicioOp.equals("1")) servicio = "VIP";
        else if (servicioOp.equals("2")) servicio = "REGULAR";
        else if (servicioOp.equals("3")) servicio = "CARGA";
        else if (servicioOp.equals("4")) servicio = "EJECUTIVO";
        else servicio = "NA";

        String tipoOp = in.leerOpcionTexto(
                "Crear Ticket\nTipo de Bus (solo 1 tipo):\n1) P Preferencial\n2) D Directo\n3) N Normal",
                "1","2","3"
        );
        char tipo = 'N';
        if (tipoOp.equals("1")) tipo = 'P';
        else if (tipoOp.equals("2")) tipo = 'D';
        else tipo = 'N';

        Ticket t = Ticket.crearNuevo(nombre, id, edad, moneda, servicio, tipo);
        cola.encolar(t);

        //guardar
        persistence.getTicketRepository().agregarTicket(t);

        javax.swing.JOptionPane.showMessageDialog(null, "Ticket creado:\n" + t.resumen());
    }

    private void llamarSiguiente() {
        Ticket t = cola.desencolar();
        if (t == null) {
            javax.swing.JOptionPane.showMessageDialog(null, "No hay tickets en cola.");
            return;
        }

        t.marcarAbordajeAhora();
        persistence.getTicketRepository().actualizarTicket(t);

        javax.swing.JOptionPane.showMessageDialog(null,
                "Siguiente:\n" + t.resumen() + "\n\nHora abordaje: " + t.getHoraAbordaje());
    }

    private void verEstado() {
        String msg =
                "=== Estado de Colas ===\n" +
                "Preferencial: " + cola.tamanoPreferencial() + "\n" +
                "Directo:      " + cola.tamanoDirecto() + "\n" +
                "Normal:       " + cola.tamanoNormal() + "\n\n" +
                "Preview (top 2):\n" +
                cola.vistaPrevia(2);

        javax.swing.JOptionPane.showMessageDialog(null, msg);
    }

    private void agregarUsuario() {
        String u = in.leerTextoNoVacio("Agregar Usuario\nUsuario:");
        if (config.existeUsuario(u)) {
            javax.swing.JOptionPane.showMessageDialog(null, "Ese usuario ya existe.");
            return;
        }
        String p = in.leerTextoNoVacio("Agregar Usuario\nContraseña:");
        config.agregarUsuario(u, p);

        //guardar config.json
        persistence.getConfigRepository().guardar(config);

        javax.swing.JOptionPane.showMessageDialog(null, "Usuario agregado y guardado en config.json.");
    }

    private void salirGuardando() {
        persistence.getConfigRepository().guardar(config);
        persistence.getTicketRepository().guardarListaCompleta();
        javax.swing.JOptionPane.showMessageDialog(null, "Guardado en JSON. Saliendo...");
    }
}

