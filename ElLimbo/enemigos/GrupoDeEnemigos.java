package enemigos;

import core.Interactuable;
import core.Personaje;
import java.util.ArrayList;

public class GrupoDeEnemigos implements Interactuable {

    private ArrayList<Enemigo> enemigos;

    public GrupoDeEnemigos(ArrayList<Enemigo> enemigos) {
        this.enemigos = enemigos;
    }

    @Override
    public boolean interactuar(Personaje personaje) {
        return false;
    }
}