/**
 * Clase encargada de centralizar el acceso a los repositorios
 * de persistencia del sistema.
 * <p>
 * Facilita la interacción con la configuración y los tickets
 * almacenados en archivos JSON.
 * </p>
 */
package com.mycompany.proyectoavance1;
public class Persistence {

    private ConfigRepository configRepository;
    private TicketRepository ticketRepository;
    private AtendidosRepository atendidosRepository;

    public Persistence() {
        configRepository = new ConfigRepository("config.json");
        ticketRepository = new TicketRepository("tiquetes.json");
        atendidosRepository = new AtendidosRepository("atendidos.json");
    }

    public ConfigRepository getConfigRepository() { return configRepository; }
    public TicketRepository getTicketRepository() { return ticketRepository; }
    // Nuevo
    public AtendidosRepository getAtendidosRepository() { return atendidosRepository; }
}