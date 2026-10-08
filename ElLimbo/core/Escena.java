package core;

import java.util.ArrayList;

public class Escena {

    private ArrayList<Opcion> opciones;
    private Interactuable interactuable;

    public Escena(ArrayList<Opcion> opciones, Interactuable interactuable) {
        this.opciones = opciones;
        this.interactuable = interactuable;
    }

    public boolean resolver(Personaje personaje) {
        return false;
    }

}
