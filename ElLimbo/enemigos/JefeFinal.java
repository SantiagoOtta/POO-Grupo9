package enemigos;

import core.Elemento;
import core.Personaje;

public class JefeFinal extends Enemigo {

    private int faseActual;
    private int cantidadFases;

    public JefeFinal(Elemento elemento, int vida, Elemento debilidad, int daño, int cantidadFases) {
        super(elemento, vida, debilidad, daño);
        this.cantidadFases = cantidadFases;
        this.faseActual = 1;
    }

    @Override
    public void infligirDaño(Personaje personaje) {

    }

    public void ataqueEspecial (Personaje personaje){

    }

    public void avanzarFase () {

    }
}

