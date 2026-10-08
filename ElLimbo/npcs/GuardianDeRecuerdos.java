package npcs;

import core.Interactuable;
import core.Personaje;
import recuerdos.Recuerdo;
import java.util.ArrayList;
import java.util.HashMap;

public class GuardianDeRecuerdos implements Interactuable {

    private static HashMap<Personaje, ArrayList<Recuerdo>> memoriaGlobal = new HashMap<>();

    @Override
    public boolean interactuar(Personaje personaje) {
        return false;
    }

    public void archivar(Recuerdo recuerdo, Personaje personaje) {

    }

    public Recuerdo reclamar(Recuerdo recuerdo) {
        return null;
    }

    public void guardarEnArchivo() {

    }
}
