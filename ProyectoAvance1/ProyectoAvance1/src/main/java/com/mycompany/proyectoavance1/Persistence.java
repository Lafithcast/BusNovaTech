////Authors = Allison Alvarado, Lafith Castrillo, Fernanda Herrera & Nicolas Peñas
package com.mycompany.proyectoavance1;
public class Persistence {

    private ConfigRepository configRepository;
    private TicketRepository ticketRepository;

    public Persistence() {
        configRepository = new ConfigRepository("config.json");
        ticketRepository = new TicketRepository("tiquetes.json");
    }

    public ConfigRepository getConfigRepository() { return configRepository; }
    public TicketRepository getTicketRepository() { return ticketRepository; }
}