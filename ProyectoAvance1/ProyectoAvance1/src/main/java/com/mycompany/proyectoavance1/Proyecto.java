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

    public Proyecto() {
        cola = new ColaPrioridad();
        entrada = new InputJOP();
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
        ListaTickets guardados = persistence.getTicketRepository().cargarTickets();
        int indice = 0;
        while (indice < guardados.tamano()){
            Ticket ticketGuardado = guardados.obtener(indice);
            if (ticketGuardado != null){
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
                    "|MENÚ |\n" +
                    "1) Crear ticket\n" +
                    "2) Llamar siguiente\n" +
                    "3) Ver estado de colas\n" +
                    "4) Agregar usuario\n" +
                    "5) Agregar Buses\n" +
                    "6) Eliminar Buses\n" +
                    "0) Salir\n\n" +
                    "Opcion:"
            );

            if (opStr == null) {
                salirGuardando();
                return;
            }

            int opcionMenu = entrada.parseEnteroSeguro(opStr, -1);

            if (opcionMenu == 1) crearTicket();
            else if (opcionMenu == 2) llamarSiguiente();
            else if (opcionMenu == 3) verEstado();
            else if (opcionMenu == 4) agregarUsuario();
            else if (opcionMenu == 5) agregarBuses();
            else if (opcionMenu == 6) eliminarBuses();
            else if (opcionMenu == 0) {
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

        String tipoOp = entrada.leerOpcionTexto(
                "Crear Ticket\nTipo de Bus (solo 1 tipo):\n1) P Preferencial\n2) D Directo\n3) N Normal",
                "1","2","3"
        );
        char tipo = 'N';
        if (tipoOp.equals("1")) tipo = 'P';
        else if (tipoOp.equals("2")) tipo = 'D';
        else tipo = 'N';

        Ticket ticket = Ticket.crearNuevo(nombre, id, edad, moneda, servicio, tipo);
        cola.encolar(ticket);

        //guardar
        persistence.getTicketRepository().agregarTicket(ticket);

        javax.swing.JOptionPane.showMessageDialog(null, "Ticket creado:\n" + ticket.resumen());
    }
    

    private void llamarSiguiente() {
        Ticket ticket = cola.desencolar();
        if (ticket == null) {
            javax.swing.JOptionPane.showMessageDialog(null, "No hay tickets en cola.");
            return;
        }

        ticket.marcarAbordajeAhora();
        persistence.getTicketRepository().actualizarTicket(t);

        javax.swing.JOptionPane.showMessageDialog(null,
                "Siguiente:\n" + ticket.resumen() + "\n\nHora abordaje: " + ticket.getHoraAbordaje());
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
    
    
    private void agregarBuses(){
        int cantidadActual = config.getCantidadBuses();
        int busesNuevos = entrada.leerEnteroRango(
        "Buses actuales: " + cantidadActual + "\n cuantos desea agregar?", 1, 100 );
        config.setCantidadBuses(cantidadActual + busesNuevos);
        persistence.getConfigRepository().guardar(config);
        javax.swing.JOptionPane.showMessageDialog(null, 
        "Se agregaron " + busesNuevos + " buses. Total actual: " + config.getCantidadBuses());
    }
    
    private void eliminarBuses(){
        int cantidadActual = config.getCantidadBuses();
        if(cantidadActual <= 3){
            javax.swing.JOptionPane.showMessageDialog(null,
            "Ya no se puede reducir mas la cantidad");
            return;
        }
        int maximoEliminar = cantidadActual - 3;
        int busesEliminar = entrada.leerEnteroRango("Buses actuales: " + cantidadActual + "\nCuantos desea eliminar? (max " + maximoEliminar + ")",
        1, maximoEliminar);
        config.setCantidadBuses(cantidadActual - busesEliminar);
        persistence.getConfigRepository().guardar(config);
        javax.swing.JOptionPane.showMessageDialog(null, 
        "Se eliminaron " + busesEliminar + " buses. Total actual: " + config.getCantidadBuses());
    }
    
    private void salirGuardando() {
        persistence.getConfigRepository().guardar(config);
        persistence.getTicketRepository().guardarListaCompleta();
        javax.swing.JOptionPane.showMessageDialog(null, "Guardado en JSON. Saliendo...");
    }
}

