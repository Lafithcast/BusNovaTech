/**
 * Clase principal de lógica del sistema.
 * <p>
 * Se encarga de coordinar el flujo general de la aplicación: carga de
 * configuración, autenticación de usuarios, manejo de tickets, colas de
 * prioridad, menú principal y persistencia.
 * </p>
 */
package com.mycompany.proyectoavance1;

public class Proyecto {

    private Configuracion config;
    private ListaBuses listaBuses;
    private InputJOP entrada;
    private Persistence persistence;

    /**
     * Constructor del sistema.
     * <p>
     * Inicializa la cola de prioridad, la lista de buses,
     * los utilitarios de entrada y los repositorios de persistencia.
     * </p>
     */
    public Proyecto() {
        listaBuses = new ListaBuses();
        entrada = new InputJOP();
        persistence = new Persistence();
        config = null;
    }

    /**
     * Inicializa los buses según la configuración cargada.
     * <p>
     * Crea los buses con números secuenciales. El bus 1 es preferencial,
     * el bus 2 es directo, y los demás son normales.
     * </p>
     */
    private void inicializarBuses() {
    ListaBuses busesGuardados = persistence.getBusesRepository().cargarBuses();

    if (busesGuardados != null && !busesGuardados.estaVacia()) {
        listaBuses = busesGuardados;
        return;
    }

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

    persistence.getBusesRepository().guardarBuses(listaBuses);
    }
    

    /**
     * Busca un bus disponible según el tipo solicitado.
     * 
     * @param tipoBus Tipo de bus a buscar ('P', 'D' o 'N')
     * @return El primer bus del tipo especificado, o {@code null} si no hay
     */
    private Bus buscarBusDisponiblePorTipo(char tipoBus) {
    Bus mejorBus = null;
    int menorCantidadEnFila = -1;
    int posicionActual = 0;

    while (posicionActual < listaBuses.tamano()) {
        Bus busActual = listaBuses.obtenerBusEnPosicion(posicionActual);

        if (busActual != null && busActual.getTipoBus() == tipoBus) {
            if (busActual.estaLibre() && busActual.getFilaEspera().estaVacia()) {
                return busActual;
            }

            int cantidadActualEnFila = busActual.cantidadEnFila();

            if (mejorBus == null || cantidadActualEnFila < menorCantidadEnFila) {
                mejorBus = busActual;
                menorCantidadEnFila = cantidadActualEnFila;
            }
        }

        posicionActual++;
    }

    return mejorBus;
}

private Bus buscarBusParaNuevoTicket(char tipoBusSolicitado) {
    return buscarBusDisponiblePorTipo(tipoBusSolicitado);
}

    /**
     * Inicia el sistema.
     * <p>
     * Carga la configuración desde config.json. Si no existe,
     * inicia el proceso de configuración desde cero. Luego inicializa los buses,
     * carga los tickets pendientes desde tiquetes.json, solicita login y muestra el menú principal.
     * </p>
     */
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

        if (!login()) {
            javax.swing.JOptionPane.showMessageDialog(null, "Demasiados intentos. saliendo.");
            return;
        }

        menu();
    }

    /**
     * Configura el sistema desde cero.
     * <p>
     * Solicita al usuario el nombre de la terminal, la cantidad de buses
     * y crea un usuario inicial.
     * </p>
     */
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

    /**
     * Solicita al usuario que inicie sesión.
     * Permite 3 intentos.
     * @return {@code true} si el login fue exitoso, {@code false} si se superaron los intentos
     */
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

    /**
     * Muestra el menú principal y procesa las opciones del usuario.
     */
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
                    + "7) Ver tickets atendidos\n"
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

    /**
     * Crea un nuevo ticket.
     * <p>
     * Solicita nombre, ID, edad, moneda, servicio y tipo de bus.
     * Busca un bus disponible del tipo solicitado y asigna el ticket
     * directamente si el bus está libre, o lo encola si está ocupado.
     * Guarda el ticket en tiquetes.json.
     * </p>
     */
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

        Bus busAsignado = buscarBusParaNuevoTicket(tipo);

        if (busAsignado == null) {
            javax.swing.JOptionPane.showMessageDialog(null, "No existe un bus disponible para ese tipo.");
            return;
        }

        if (busAsignado.estaLibre() && busAsignado.getFilaEspera().estaVacia()) {
            busAsignado.asignarAtencionDirecta(ticket);
            persistence.getTicketRepository().agregarTicket(ticket);
            persistence.getBusesRepository().guardarBuses(listaBuses);

            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Ticket creado y enviado directamente a atencion.\n\nBus asignado: "
                    + busAsignado.getNumeroBus() + "\n" + ticket.resumen()
            );
        } else {
            busAsignado.agregarAFila(ticket);
            persistence.getTicketRepository().agregarTicket(ticket);
            persistence.getBusesRepository().guardarBuses(listaBuses);

            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Ticket creado y agregado a la fila del bus.\n\nBus asignado: "
                    + busAsignado.getNumeroBus() + "\n" + ticket.resumen()
            );
        }
    }

    /**
     * Obtiene el monto a cobrar según el servicio.
     * @param servicio El tipo de servicio
     * @return El monto correspondiente al servicio, o 0 si es desconocido
     */
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

    /**
     * Atiende al siguiente ticket del bus.
     * <p>
     * Solicita el número de bus, verifica si hay ticket en fila,
     * muestra el monto a pagar, registra el pago y marca el ticket como atendido.
     * Si el cliente paga, el ticket pasa a atendidos.json.
     * </p>
     */
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
        persistence.getBusesRepository().guardarBuses(listaBuses);
    }

    /**
     * Muestra el estado actual de todos los buses.
     */
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

    /**
     * Agrega un nuevo usuario al sistema.
     * <p>
     * Solicita nombre de usuario y contraseña, verifica que no exista
     * y guarda los cambios en config.json.
     * </p>
     */
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

    /**
     * Agrega nuevos buses al sistema.
     * <p>
     * Solicita la cantidad de buses a agregar, los crea y actualiza la configuración en config.json.
     * </p>
     */
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
        persistence.getBusesRepository().guardarBuses(listaBuses);

        javax.swing.JOptionPane.showMessageDialog(
                null,
                "Se agregaron " + busesNuevos + " buses. Total actual: " + config.getCantidadBuses()
        );
    }

    /**
     * Elimina buses del sistema.
     * <p>
     * Solo permite eliminar hasta dejar un mínimo de 3 buses.
     * No elimina buses que tengan tickets pendientes.
     * Actualiza la configuración en config.json.
     * </p>
     */
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
        persistence.getBusesRepository().guardarBuses(listaBuses);

        javax.swing.JOptionPane.showMessageDialog(
                null,
                "Se eliminaron " + eliminados + " buses. Total actual: " + config.getCantidadBuses()
        );
    }

    /**
     * Guarda todos los datos y sale del sistema.
     */
    private void salirGuardando() {
        persistence.getConfigRepository().guardar(config);
        persistence.getTicketRepository().guardarListaCompleta();
        persistence.getAtendidosRepository().guardarListaCompleta();
        persistence.getBusesRepository().guardarBuses(listaBuses);
        
        javax.swing.JOptionPane.showMessageDialog(null, "Guardado en JSON. Saliendo...");
    }

    /**
     * Muestra la lista de tickets atendidos.
     * Carga los tickets desde atendidos.json y los muestra.
     */
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
