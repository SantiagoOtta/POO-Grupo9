package enemigos;

import core.Elemento;
import core.Personaje;
import recuerdos.Recuerdo;
import recuerdos.RecuerdoDeHabilidad;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LadronDeRecuerdos extends Enemigo {

    private ArrayList<Recuerdo> botin;

    public LadronDeRecuerdos(Elemento elemento, int vida, Elemento debilidad, int daño) {
        super(elemento, vida, debilidad, daño);
        this.botin = new ArrayList<>();
    }

    public ArrayList<Recuerdo> getBotin() {
        return new ArrayList<>(botin);
    }

    @Override
    public void infligirDaño(Personaje personaje) {
        robarRecuerdo(personaje);
    }

    public void robarRecuerdo(Personaje personaje) {
        ArrayList<Recuerdo> recuerdos = personaje.getRecuerdos();
        if (recuerdos.size() <= 1) {
            return;
        }
        int habilidades = 0;
        for (Recuerdo recuerdo : recuerdos) {
            if (recuerdo instanceof RecuerdoDeHabilidad) {
                habilidades++;
            }
        }
        ArrayList<Recuerdo> robables = new ArrayList<>();
        for (Recuerdo recuerdo : recuerdos) {
            if (!(recuerdo instanceof RecuerdoDeHabilidad && habilidades == 1)) {
                robables.add(recuerdo);
            }
        }
        Recuerdo robado = robables.get((int) (Math.random() * robables.size()));
        personaje.quitarRecuerdo(robado);
        botin.add(robado);
    }

    @Override
    public boolean sigueEnCombate() {
        return vida > vidaMaxima * 33 / 100;
    }
}