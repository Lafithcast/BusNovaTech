/**
 * Clase principal de lógica del sistema.
 * <contrasena>
 * Se encarga de coordinar el flujo general de la aplicación:
 * carga de configuración, autenticación de usuarios, manejo
 * de tickets, colas de prioridad, menú principal y persistencia.
 * </contrasena>
 */

package com.mycompany.proyectoavance1;
public class Proyecto {

    private Configuracion config;
    private ColaPrioridad cola;
    private InputJOP entrada;
    private Persistence persistence;
    // Nuevo
    private Bus[] buses;

    public Proyecto() {
        cola = new ColaPrioridad();
        entrada = new InputJOP();
        persistence = new Persistence();
        config = null;
        buses = null;
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

        // Inicializar buses
        inicializarBuses();

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
        String nombreTerminal = entrada.leerTextoNoVacio("Nombre de la terminal:");
        int totalBuses = entrada.leerEnteroRango("Cantidad total de buses :\nExcluyendo 1 normal y 1 preferencial.",
                3, 200
        );

        config.setNombreTerminal(nombreTerminal);
        config.setCantidadBuses(totalBuses);

        javax.swing.JOptionPane.showMessageDialog(null, "Crea un usuario inicial");
        String usuario = entrada.leerTextoNoVacio("Usuario:");
        String contrasena = entrada.leerTextoNoVacio("Contraseña:");
        config.agregarUsuario(usuario, contrasena);
    }

    // Nuevo método
    private void inicializarBuses() {
        int cant = config.getCantidadBuses();
        buses = new Bus[cant];
        for (int i = 0; i < cant; i++) {
            buses[i] = new Bus(i + 1);
        }
    }

    private boolean login() {
        int intentos = 0;
        while (intentos < 3) {
            String usuario = entrada.leerTextoNoVacio("LOGIN\nUsuario:");
            String contrasena = entrada.leerTextoNoVacio("LOGIN\nContraseña:");

            if (config.validarLogin(usuario, contrasena)) {
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
                    "5) Abordar (atender siguiente en bus)\n" +
                    "6) Ver tickets atendidos\n" +
                    "0) Salir\n\n" +
                    "Opcion:"
            );

            if (opStr == null) {
                continue;
            }

            int op = entrada.parseEnteroSeguro(opStr, -1);

            if (op == 1) crearTicket();
            else if (op == 2) llamarSiguiente();
            else if (op == 3) verEstado();
            else if (op == 4) agregarUsuario();
            else if (op == 5) abordar();
            else if (op == 6) verAtendidos();
            else if (op == 0) {
                salirGuardando();
                return;
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Intenta de nuevo.");
            }
        }
    }

    private void crearTicket() {
        String nombre = entrada.leerTextoNoVacio("Crear Ticket\nNombre:");
        int id = entrada.leerEnteroRango("Crear Ticket\nID:", 1, 999999999);
        int edad = entrada.leerEnteroRango("Crear Ticket\nEdad:", 0, 120);

        String monedaOp = entrada.leerOpcionTexto(
                "Crear Ticket\nMoneda/Cuenta:\n1) COLONES\n2) DOLARES",
                "1", "2"
        );
        String moneda = monedaOp.equals("1") ? "COLONES" : "DOLARES";

        String servicioOp = entrada.leerOpcionTexto(
                "Crear Ticket\nServicio:\n1) VIP\n2) REGULAR\n3) CARGA\n4) EJECUTIVO\n5) NA",
                "1","2","3","4","5"
        );
        String servicio = "NA";
        if (servicioOp.equals("1")) servicio = "VIP";
        else if (servicioOp.equals("2")) servicio = "REGULAR";
        else if (servicioOp.equals("3")) servicio = "CARGA";
        else if (servicioOp.equals("4")) servicio = "EJECUTIVO";
        else servicio = "NA";

        int libras = 0;
        if (servicio.equals("CARGA")) {
            libras = in.leerEnteroRango("Crear Ticket\nLibras de carga:", 0, 1000);
        }

        String tipoOp = in.leerOpcionTexto(
                "Crear Ticket\nTipo de Bus (solo 1 tipo):\n1) P Preferencial\n2) D Directo\n3) N Normal",
                "1","2","3"
        );
        char tipo = 'N';
        if (tipoOp.equals("1")) tipo = 'P';
        else if (tipoOp.equals("2")) tipo = 'D';
        else tipo = 'N';

        // Asignar bus
        int busId = in.leerEnteroRango("Crear Ticket\nAsignar a bus (1-" + buses.length + "):", 1, buses.length);

        Ticket t = Ticket.crearNuevo(nombre, id, edad, moneda, servicio, tipo);
        t.setTerminalCompra(config.getNombreTerminal());
        t.setBusAsignado(busId);
        t.setLibrasCarga(libras);

        Bus bus = buses[busId - 1];
        bus.getCola().encolar(t);

        // Si inspector libre, atender inmediatamente
        if (!bus.getInspector().isOcupado()) {
            atenderTicket(t, bus);
        }

        //guardar en tiquetes.json
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
        String usuario = entrada.leerTextoNoVacio("Agregar Usuario\nUsuario:");
        if (config.existeUsuario(usuario)) {
            javax.swing.JOptionPane.showMessageDialog(null, "Ese usuario ya existe.");
            return;
        }
        String contrasena = entrada.leerTextoNoVacio("Agregar Usuario\nContraseña:");
        config.agregarUsuario(usuario, contrasena);

        //guardar config.json
        persistence.getConfigRepository().guardar(config);

        javax.swing.JOptionPane.showMessageDialog(null, "Usuario agregado y guardado en config.json.");
    }

    // Nuevo método
    private void abordar() {
        int busId = in.leerEnteroRango("Abordar\nElegir bus (1-" + buses.length + "):", 1, buses.length);
        Bus bus = buses[busId - 1];
        Ticket t = bus.getCola().desencolar();
        if (t == null) {
            javax.swing.JOptionPane.showMessageDialog(null, "No hay tickets en la cola del bus " + busId);
            return;
        }
        atenderTicket(t, bus);
    }

    // Nuevo método
    private void atenderTicket(Ticket t, Bus bus) {
        bus.getInspector().setOcupado(true);
        int costo = calcularCosto(t);
        String msg = "Atendiendo ticket:\n" + t.resumen() + "\nCosto: $" + costo + "\n¿Desea pagar? (si/no)";
        String paga = in.leerOpcionTexto(msg, "si", "no");
        if (paga.equals("si")) {
            t.setEstado("Atendido");
            t.marcarAbordajeAhora(); // horaAtencion
            persistence.getAtendidosRepository().agregarTicket(t);
            javax.swing.JOptionPane.showMessageDialog(null, "Ticket atendido y guardado en atendidos.json");
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "Pago rechazado. Ticket removido de la cola.");
        }
        bus.getInspector().setOcupado(false);
    }

    // Nuevo método
    private int calcularCosto(Ticket t) {
        if (t.servicio.equals("VIP")) return 100;
        if (t.servicio.equals("REGULAR")) return 20;
        if (t.servicio.equals("CARGA")) return 20 + 10 * t.getLibrasCarga();
        if (t.servicio.equals("EJECUTIVO")) return 1000;
        return 0;
    }

    // Nuevo método
    private void verAtendidos() {
        Ticket[] atendidos = persistence.getAtendidosRepository().cargarTickets();
        String msg = "=== Tickets Atendidos ===\n";
        for (int i = 0; i < atendidos.length; i++) {
            if (atendidos[i] != null) {
                msg += atendidos[i].resumen() + "\n\n";
            }
        }
        if (atendidos.length == 0) msg += "(Ninguno)\n";
        javax.swing.JOptionPane.showMessageDialog(null, msg);
    }

    private void salirGuardando() {
        persistence.getConfigRepository().guardar(config);
        persistence.getTicketRepository().guardarListaCompleta();
        persistence.getAtendidosRepository().guardarListaCompleta();
        javax.swing.JOptionPane.showMessageDialog(null, "Guardado en JSON. Saliendo...");
    }
}

