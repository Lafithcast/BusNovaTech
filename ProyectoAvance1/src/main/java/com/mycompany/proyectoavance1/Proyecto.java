/**
 * Clase principal de lógica del sistema.
 * <p>
 * Se encarga de coordinar el flujo general de la aplicación:
 * carga de configuración, autenticación de usuarios, manejo
 * de tickets, colas de prioridad, menú principal y persistencia.
 * </p>
 */

package com.mycompany.proyectoavance1;
public class Proyecto {
    private Configuracion config;
    private ColaPrioridad cola;
    private InputJOP Input;
    private Persistence persistence;
    private ListaBuses buses;

    public Proyecto() {
        cola = new ColaPrioridad();
        Input = new InputJOP();
        persistence = new Persistence();
        config = null;
        buses = new ListaBuses();
    }

    public void iniciar() {
        javax.swing.JOptionPane.showMessageDialog(null, "Bienvenido al Sistema");

        config = persistence.getConfigRepository().cargar();
        if (config == null) {
            javax.swing.JOptionPane.showMessageDialog(null, "config.json no existe o esta dañado.");
            config = new Configuracion();
            configurarDesdeCero();
            persistence.getConfigRepository().guardar(config);
        }

        inicializarBuses();

        NodoTicket actual = persistence.getTicketRepository().cache.getCabeza();
        while (actual != null) {
            Ticket ticket = actual.getValor();
            if (ticket != null) {
                cola.encolar(ticket);
            }
            actual = actual.getSiguiente();
        }

        if (!login()) {
            javax.swing.JOptionPane.showMessageDialog(null, "Demasiados intentos. saliendo.");
            return;
        }

        menu();
    }

    private void configurarDesdeCero() {
        String nombreTerminal = Input.leerTextoNoVacio("Nombre de la terminal:");
        int totalBuses = Input.leerEnteroRango(
                "Cantidad total de buses :\nExcluyendo 1 normal y 1 preferencial.",
                3,
                200
        );

        config.setNombreTerminal(nombreTerminal);
        config.setCantidadBuses(totalBuses);

        javax.swing.JOptionPane.showMessageDialog(null, "Crea un usuario inicial");
        String u = Input.leerTextoNoVacio("Usuario:");
        String p = Input.leerTextoNoVacio("Contraseña:");
        config.agregarUsuario(u, p);
    }

    private void inicializarBuses() {
        buses = new ListaBuses();

        int cant = config.getCantidadBuses();
        int i = 1;

        while (i <= cant) {
            buses.agregar(new Bus(i));
            i++;
        }
    }

    private boolean login() {
        int intentos = 0;

        while (intentos < 3) {
            String usuario = Input.leerTextoNoVacio("LOGIN\nUsuario:");
            String password = Input.leerTextoNoVacio("LOGIN\nContraseña:");

            if (config.validarLogin(usuario, password)) {
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
            String Opcion = javax.swing.JOptionPane.showInputDialog(
                    "NOVATECH \n" +
                    "1) Crear ticket\n" +
                    "2) Llamar siguiente\n" +
                    "3) Ver estado de colas\n" +
                    "4) Agregar usuario\n" +
                    "5) Abordar (atender siguiente en bus)\n" +
                    "6) Ver tickets atendidos\n" +
                    "0) Salir\n\n" +
                    "Opcion:"
            );

            if (Opcion == null) {
                continue;
            }

            int op = Input.parseEnteroSeguro(Opcion, -1);

            if (op == 1) {
                crearTicket();
            } else if (op == 2) {
                llamarSiguiente();
            } else if (op == 3) {
                verEstado();
            } else if (op == 4) {
                agregarUsuario();
            } else if (op == 5) {
                abordar();
            } else if (op == 6) {
                verAtendidos();
            } else if (op == 0) {
                salirGuardando();
                return;
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Intenta de nuevo.");
            }
        }
    }

    private void crearTicket() {
    String nombre = Input.leerTextoNoVacio("Crear Ticket\nNombre:");
    int id = Input.leerEnteroRango("Crear Ticket\nID:", 1, 9999999);
    int edad = Input.leerEnteroRango("Crear Ticket\nEdad:", 0, 120);

    String monedaOpcion = Input.leerOpcionTexto(
            "Crear Ticket\nMoneda/Cuenta:\n1) COLONES\n2) DOLARES",
            "1", "2"
    );

    String moneda = "DOLARES";
    if (monedaOpcion.equals("1")) {
        moneda = "COLONES";
    }

    String servicioOp = Input.leerOpcionTexto(
            "Crear Ticket\nServicio:\n1) VIP\n2) REGULAR\n3) CARGA\n4) EJECUTIVO\n5) NA",
            "1", "2", "3", "4", "5"
    );

    String servicio = "NA";
    if (servicioOp.equals("1")) {
        servicio = "VIP";
    } else if (servicioOp.equals("2")) {
        servicio = "REGULAR";
    } else if (servicioOp.equals("3")) {
        servicio = "CARGA";
    } else if (servicioOp.equals("4")) {
        servicio = "EJECUTIVO";
    }

    int libras = 0;
    if (servicio.equals("CARGA")) {
        libras = Input.leerEnteroRango("Crear Ticket\nLibras de carga:", 0, 1000);
    }

    String tipoOp = Input.leerOpcionTexto(
            "Crear Ticket\nTipo de Bus (solo 1 tipo):\n1) P Preferencial\n2) D Directo\n3) N Normal",
            "1", "2", "3"
    );

    char tipo = 'N';
    if (tipoOp.equals("1")) {
        tipo = 'P';
    } else if (tipoOp.equals("2")) {
        tipo = 'D';
    }

    int busId = Input.leerEnteroRango(
            "Crear Ticket\nAsignar a bus (1-" + buses.tamano() + "):",
            1,
            buses.tamano()
    );

    Ticket ticket = Ticket.crearNuevo(nombre, id, edad, moneda, servicio, tipo);
    ticket.setTerminalCompra(config.getNombreTerminal());
    ticket.setBusAsignado(busId);
    ticket.setLibrasCarga(libras);

    Bus bus = buses.buscarPorId(busId);

    if (bus == null) {
        javax.swing.JOptionPane.showMessageDialog(null, "Bus no encontrado.");
        return;
    }

    bus.getCola().encolar(ticket);
    cola.encolar(ticket);
    persistence.getTicketRepository().agregarTicket(ticket);

    javax.swing.JOptionPane.showMessageDialog(null,
            "Ticket creado y agregado a la cola del bus.\n" + ticket.resumen()
    );
}

    private void llamarSiguiente() {
        Ticket ticket = cola.desencolar();

        if (ticket == null) {
            javax.swing.JOptionPane.showMessageDialog(null, "No hay tickets en cola.");
            return;
        }

        ticket.marcarAbordajeAhora();
        persistence.getTicketRepository().actualizarTicket(ticket);

        javax.swing.JOptionPane.showMessageDialog(null,
                "Siguiente:\n" + ticket.resumen() + "\n\nHora abordaje: " + ticket.getHoraAbordaje()
        );
    }

    private void verEstado() {
        String msg =
                "=== Estado de Colas ===\n" +
                "Preferencial: " + cola.tamanoPreferencial() + "\n" +
                "Directo: " + cola.tamanoDirecto() + "\n" +
                "Normal: " + cola.tamanoNormal() + "\n\n" +
                "Preview (top 2):\n" +
                cola.vistaPrevia(2);

        javax.swing.JOptionPane.showMessageDialog(null, msg);
    }

    private void agregarUsuario() {
        String usuario = Input.leerTextoNoVacio("Agregar Usuario\nUsuario:");

        if (config.existeUsuario(usuario)) {
            javax.swing.JOptionPane.showMessageDialog(null, "Ese usuario ya existe.");
            return;
        }

        String Contrasena = Input.leerTextoNoVacio("Agregar Usuario\nContraseña:");
        config.agregarUsuario(usuario, Contrasena);

        persistence.getConfigRepository().guardar(config);

        javax.swing.JOptionPane.showMessageDialog(null, "Usuario agregado y guardado en config.json.");
    }

    private void abordar() {
        int busId = Input.leerEnteroRango(
                "Abordar\nElegir bus (1-" + buses.tamano() + "):",
                1,
                buses.tamano()
        );

        Bus bus = buses.buscarPorId(busId);

        if (bus == null) {
            javax.swing.JOptionPane.showMessageDialog(null, "Bus no encontrado.");
            return;
        }

        Ticket ticket = bus.getCola().desencolar();

        if (ticket == null) {
            javax.swing.JOptionPane.showMessageDialog(null, "No hay tickets en la cola del bus " + busId);
            return;
        }

        atenderTicket(ticket, bus);
    }

    private void atenderTicket(Ticket ticket, Bus bus) {
        bus.getInspector().setOcupado(true);

        int costo = calcularCosto(ticket);

        String msg =
                "Atendiendo ticket:\n" +
                ticket.resumen() +
                "\nCosto: $" + costo +
                "\n¿Desea pagar? (si/no)";

        String paga = Input.leerOpcionTexto(msg, "si", "no");

        if (paga.equals("si")) {
            ticket.setEstado("Atendido");
            ticket.marcarAbordajeAhora();

            persistence.getTicketRepository().actualizarTicket(ticket);
            persistence.getAtendidosRepository().agregarTicket(ticket);

            javax.swing.JOptionPane.showMessageDialog(null, "Ticket atendido y guardado en atendidos.json");
        } else {
            ticket.setEstado("Pendiente");
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "El cliente no pagó. Debe iniciar el proceso nuevamente."
            );
        }

        bus.getInspector().setOcupado(false);
    }

    private int calcularCosto(Ticket ticket) {
        if (ticket.servicio.equals("VIP")) {
            return 100;
        }

        if (ticket.servicio.equals("REGULAR")) {
            return 20;
        }

        if (ticket.servicio.equals("CARGA")) {
            return 20 + (10 * ticket.getLibrasCarga());
        }

        if (ticket.servicio.equals("EJECUTIVO")) {
            return 1000;
        }

        return 0;
    }

    private void verAtendidos() {
        String msg = "=== Tickets Atendidos ===\n";
        NodoTicket actual = persistence.getAtendidosRepository().getLista().getCabeza();

        if (actual == null) {
            msg += "(Ninguno)\n";
        } else {
            while (actual != null) {
                Ticket ticket = actual.getValor();
                if (ticket != null) {
                    msg += ticket.resumen() + "\n\n";
                }
                actual = actual.getSiguiente();
            }
        }

        javax.swing.JOptionPane.showMessageDialog(null, msg);
    }

    private void salirGuardando() {
        persistence.getConfigRepository().guardar(config);
        persistence.getTicketRepository().guardarListaCompleta();
        persistence.getAtendidosRepository().guardarListaCompleta();

        javax.swing.JOptionPane.showMessageDialog(null, "Cambios guardados. Hasta luego.");
    }
}

