package npcs;

import core.Interactuable;
import core.Personaje;
import recuerdos.Recuerdo;
import recuerdos.Reutilizable;

public class Mercader implements Interactuable {

    private Recuerdo[] stock;

    public Mercader() {
        this.stock = new Recuerdo[3];
    }

    @Override
    public boolean interactuar(Personaje personaje) {
        return false;
    }

    public boolean comprar(Recuerdo recuerdo, Personaje personaje) {
        return false;
    }

    public boolean recargar(Reutilizable reutilizable, Personaje personaje) {
        return false;
    }
}
