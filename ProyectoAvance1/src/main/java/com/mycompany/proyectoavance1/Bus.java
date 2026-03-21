package com.mycompany.proyectoavance1;

public class Bus {
    private int id;
    private Inspector inspector;
    private ColaSimple cola;

    public Bus(int id) {
        this.id = id;
        inspector = new Inspector();
        cola = new ColaSimple();
    }

    public int getId() {
        return id;
    }

    public Inspector getInspector() {
        return inspector;
    }

    public ColaSimple getCola() {
        return cola;
    }
}