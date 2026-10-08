package enemigos;

import core.Elemento;
import core.Personaje;

public class EnemigoBase extends Enemigo {

    public EnemigoBase(Elemento elemento, int vida, Elemento debilidad, int daño) {
        super(elemento, vida, debilidad, daño);
    }

    @Override
    public void infligirDaño(Personaje personaje) {

    }
}
