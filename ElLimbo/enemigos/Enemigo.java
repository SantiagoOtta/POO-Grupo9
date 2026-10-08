package enemigos;

import core.Elemento;
import core.Interactuable;
import core.Personaje;

public abstract class Enemigo implements Interactuable {
    protected Elemento elemento;
    protected int vida;
    protected Elemento debilidad;
    protected int daño;

    public Enemigo(Elemento elemento, int vida, Elemento debilidad, int daño) {
        this.elemento = elemento;
        this.vida = vida;
        this.debilidad = debilidad;
        this.daño = daño;
    }

    @Override
    public boolean interactuar(Personaje personaje) {
        return false;
    }

    public abstract void infligirDaño(Personaje personaje);

    public void recibirDaño(int cantidad, Elemento elemento) {

    }
}
