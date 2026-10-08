package enemigos;

import core.Elemento;
import core.Personaje;
import recuerdos.Recuerdo;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LadronDeRecuerdos extends Enemigo {

    private ArrayList<Recuerdo> botin;

    public LadronDeRecuerdos(Elemento elemento, int vida, Elemento debilidad, int daño) {
        super(elemento, vida, debilidad, daño);
        this.botin = new ArrayList<>();
    }

    @Override
    public void infligirDaño(Personaje personaje) {

    }

    public void robarRecuerdos(Personaje personaje) {

    }

}
