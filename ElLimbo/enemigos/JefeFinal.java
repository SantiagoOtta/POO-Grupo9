package enemigos;

import core.Elemento;
import core.Personaje;

public class JefeFinal extends Enemigo {

    private int faseActual;
    private int cantidadFases;
    private int turnosJugados;

    public JefeFinal(Elemento elemento, int vida, Elemento debilidad, int daño) {
        super(elemento, vida, debilidad, daño);
        this.faseActual = 1;
        this.cantidadFases = 2;
        this.turnosJugados = 0;
    }

    @Override
    public void infligirDaño(Personaje personaje) {

    }

    public void ataqueEspecial(Personaje personaje) {

    }

    public void avanzarFase() {

    }
}