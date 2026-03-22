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
    private ListaBuses listaBuses;
    private InputJOP entrada;
    private Persistence persistence;

    public Proyecto() {
        cola = new ColaPrioridad();
        listaBuses = new ListaBuses();
        entrada = new InputJOP();
        persistence = new Persistence();
        config = null;
    }

    private void inicializarBuses() {
        listaBuses = new ListaBuses();

        int numeroBus = 1;
        while (numeroBus <= config.getCantidadBuses()) {
            char tipoBus = 'N';

            if (numeroBus == 1) {
                tipoBus = 'P';
            } else if (numeroBus == 2) {
                tipoBus = 'D';
            }

            Bus nuevoBus = new Bus(numeroBus, tipoBus);
            listaBuses.agregarBus(nuevoBus);
            numeroBus++;
        }
    }

    private Bus buscarBusDisponiblePorTipo(char tipoBus) {
        int posicion = 0;

        while (posicion < listaBuses.tamano()) {
            Bus busActual = listaBuses.obtenerBusEnPosicion(posicion);

            if (busActual != null && busActual.getTipoBus() == tipoBus) {
                return busActual;
            }

            posicion++;
        }

        return null;
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

        inicializarBuses();

        //tiquetes.json
        ListaTickets guardados = persistence.getTicketRepository().cargarTickets();
        int indice = 0;
        while (indice < guardados.tamano()) {
            Ticket ticketGuardado = guardados.obtener(indice);
            if (ticketGuardado != null) {
                cola.encolar(ticketGuardado);
            }
            indice++;
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
                    "NOVATECH\n"
                    + "1) Crear ticket\n"
                    + "2) Abordar Ticket\n"
                    + "3) Ver estado de buses\n"
                    + "4) Agregar usuario\n"
                    + "5) Agregar Buses\n"
                    + "6) Eliminar Buses\n"
                    + "7) Ver tickets atendidos"
                    + "0) Salir\n\n"
                    + "Opcion:"
            );

            if (opStr == null) {
                salirGuardando();
                return;
            }

            int opcionMenu = entrada.parseEnteroSeguro(opStr, -1);

            if (opcionMenu == 1) {
                crearTicket();
            } else if (opcionMenu == 2) {
                llamarSiguiente();
            } else if (opcionMenu == 3) {
                verEstado();
            } else if (opcionMenu == 4) {
                agregarUsuario();
            } else if (opcionMenu == 5) {
                agregarBuses();
            } else if (opcionMenu == 6) {
                eliminarBuses();
            } else if (opcionMenu == 7) {
                verAtendidos();
            } else if (opcionMenu == 0) {
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
        } else {
            servicio = "NA";
        }

        String tipoOp = entrada.leerOpcionTexto(
                "Crear Ticket\nTipo de Bus (solo 1 tipo):\n1) P Preferencial\n2) D Directo\n3) N Normal",
                "1", "2", "3"
        );

        char tipo = 'N';
        if (tipoOp.equals("1")) {
            tipo = 'P';
        } else if (tipoOp.equals("2")) {
            tipo = 'D';
        } else {
            tipo = 'N';
        }

        Ticket ticket = Ticket.crearNuevo(nombre, id, edad, moneda, servicio, tipo);
        ticket.setTerminalCompra(config.getNombreTerminal());

        Bus busAsignado = buscarBusDisponiblePorTipo(tipo);

        if (busAsignado == null) {
            javax.swing.JOptionPane.showMessageDialog(null, "No existe un bus disponible para ese tipo.");
            return;
        }

        if (busAsignado.estaLibre() && busAsignado.getFilaEspera().estaVacia()) {
            busAsignado.asignarAtencionDirecta(ticket);
            persistence.getTicketRepository().agregarTicket(ticket);

            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Ticket creado y enviado directamente a atencion.\n\nBus asignado: "
                    + busAsignado.getNumeroBus() + "\n" + ticket.resumen()
            );
        } else {
            busAsignado.agregarAFila(ticket);
            persistence.getTicketRepository().agregarTicket(ticket);

            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Ticket creado y agregado a la fila del bus.\n\nBus asignado: "
                    + busAsignado.getNumeroBus() + "\n" + ticket.resumen()
            );
        }
    }

    private double obtenerMontoPorServicio(String servicio) {
        if (servicio == null) {
            return 0;
        }

        if (servicio.equals("VIP")) {
            return 5000;
        } else if (servicio.equals("REGULAR")) {
            return 3000;
        } else if (servicio.equals("CARGA")) {
            return 7000;
        } else if (servicio.equals("EJECUTIVO")) {
            return 6000;
        }

        return 0;
    }

    private void llamarSiguiente() {
        int numeroBus = entrada.leerEnteroRango("Abordar Ticket\nDigite el numero del bus:", 1, config.getCantidadBuses());

        Bus busSeleccionado = listaBuses.buscarBusPorNumero(numeroBus);

        if (busSeleccionado == null) {
            javax.swing.JOptionPane.showMessageDialog(null, "No existe un bus con ese numero.");
            return;
        }

        Ticket ticketAtender = busSeleccionado.getTicketEnAtencion();

        if (ticketAtender == null) {
            if (busSeleccionado.getFilaEspera().estaVacia()) {
                javax.swing.JOptionPane.showMessageDialog(null, "Ese bus no tiene tickets en espera.");
                return;
            }

            Ticket siguienteTicket = busSeleccionado.getFilaEspera().sacarPrimero();
            busSeleccionado.asignarAtencionDirecta(siguienteTicket);
            ticketAtender = busSeleccionado.getTicketEnAtencion();
        }

        double montoCobrado = obtenerMontoPorServicio(ticketAtender.getServicio());

        String respuestaPago = entrada.leerOpcionTexto(
                "Abordar Ticket\nBus: " + busSeleccionado.getNumeroBus()
                + "\nCliente: " + ticketAtender.getNombre()
                + "\nServicio: " + ticketAtender.getServicio()
                + "\nMonto a pagar: " + montoCobrado
                + "\n\nEl cliente pago?\n1) Si\n2) No",
                "1", "2"
        );

        boolean pagado = respuestaPago.equals("1");

        ticketAtender.marcarAtencion(
                config.getNombreTerminal(),
                busSeleccionado.getNumeroBus(),
                montoCobrado,
                pagado
        );

        persistence.getTicketRepository().actualizarTicket(ticketAtender);

        if (pagado) {
            persistence.getAtendidosRepository().agregarAtendido(ticketAtender);

            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Ticket atendido correctamente.\n\n" + ticketAtender.resumen()
            );
        } else {
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "El cliente no pago.\nDebe iniciar el proceso nuevamente.\n\n" + ticketAtender.resumen()
            );
        }

        busSeleccionado.finalizarAtencion();
    }

    private void verEstado() {
        if (listaBuses == null || listaBuses.estaVacia()) {
            javax.swing.JOptionPane.showMessageDialog(null, "No hay buses registrados.");
            return;
        }

        String mensaje = "=== Estado de Buses ===\n\n";

        int posicion = 0;
        while (posicion < listaBuses.tamano()) {
            Bus busActual = listaBuses.obtenerBusEnPosicion(posicion);

            if (busActual != null) {
                mensaje += "Bus #" + busActual.getNumeroBus();
                mensaje += " | Tipo: " + busActual.getTipoBus();

                if (busActual.getTicketEnAtencion() != null) {
                    mensaje += " | En atencion: " + busActual.getTicketEnAtencion().getNombre();
                } else {
                    mensaje += " | En atencion: Ninguno";
                }

                mensaje += " | En fila: " + busActual.getFilaEspera().tamano();
                mensaje += "\n";
            }

            posicion++;
        }

        javax.swing.JOptionPane.showMessageDialog(null, mensaje);
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

    private void agregarBuses() {
        int cantidadActual = config.getCantidadBuses();

        int busesNuevos = entrada.leerEnteroRango(
                "Buses actuales: " + cantidadActual + "\nCuantos desea agregar?",
                1, 100
        );

        int numeroMayor = listaBuses.obtenerNumeroMayorBus();
        int contador = 1;

        while (contador <= busesNuevos) {
            int nuevoNumeroBus = numeroMayor + contador;
            char tipoBus = 'N';

            if (nuevoNumeroBus == 1) {
                tipoBus = 'P';
            } else if (nuevoNumeroBus == 2) {
                tipoBus = 'D';
            }

            Bus nuevoBus = new Bus(nuevoNumeroBus, tipoBus);
            listaBuses.agregarBus(nuevoBus);
            contador++;
        }

        config.setCantidadBuses(cantidadActual + busesNuevos);
        persistence.getConfigRepository().guardar(config);

        javax.swing.JOptionPane.showMessageDialog(
                null,
                "Se agregaron " + busesNuevos + " buses. Total actual: " + config.getCantidadBuses()
        );
    }

    private void eliminarBuses() {
        int cantidadActual = config.getCantidadBuses();

        if (cantidadActual <= 3) {
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Ya no se puede reducir mas la cantidad."
            );
            return;
        }

        int maximoEliminar = cantidadActual - 3;

        int busesEliminar = entrada.leerEnteroRango(
                "Buses actuales: " + cantidadActual
                + "\nCuantos desea eliminar? (max " + maximoEliminar + ")",
                1, maximoEliminar
        );

        int eliminados = 0;

        while (eliminados < busesEliminar) {
            Bus ultimoBus = listaBuses.obtenerUltimoBus();

            if (ultimoBus == null) {
                break;
            }

            if (!ultimoBus.estaCompletamenteVacio()) {
                javax.swing.JOptionPane.showMessageDialog(
                        null,
                        "No se puede eliminar el bus #" + ultimoBus.getNumeroBus()
                        + " porque tiene tickets en atencion o en fila."
                );
                break;
            }

            boolean eliminado = listaBuses.eliminarUltimoBus();

            if (!eliminado) {
                break;
            }

            eliminados++;
        }

        config.setCantidadBuses(cantidadActual - eliminados);
        persistence.getConfigRepository().guardar(config);

        javax.swing.JOptionPane.showMessageDialog(
                null,
                "Se eliminaron " + eliminados + " buses. Total actual: " + config.getCantidadBuses()
        );
    }

    private void salirGuardando() {
        persistence.getConfigRepository().guardar(config);
        persistence.getTicketRepository().guardarListaCompleta();
        persistence.getAtendidosRepository().guardarListaCompleta();
        javax.swing.JOptionPane.showMessageDialog(null, "Guardado en JSON. Saliendo...");
    }

    private void verAtendidos() {
        ListaTickets listaAtendidos = persistence.getAtendidosRepository().obtenerAtendidos();

        if (listaAtendidos == null || listaAtendidos.estaVacia()) {
            javax.swing.JOptionPane.showMessageDialog(null, "No hay tickets atendidos registrados.");
            return;
        }

        String mensaje = "=== Tickets Atendidos ===\n\n";

        int posicion = 0;
        while (posicion < listaAtendidos.tamano()) {
            Ticket ticketActual = listaAtendidos.obtener(posicion);

            if (ticketActual != null) {
                mensaje += (posicion + 1) + ") " + ticketActual.resumen() + "\n\n";
            }

            posicion++;
        }

        javax.swing.JOptionPane.showMessageDialog(null, mensaje);
    }
}
