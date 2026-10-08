package recuerdos;

import core.Personaje;
import core.Elemento;

public class RecuerdoDeTrauma extends Recuerdo {

    private Elemento elemento;
    private int daño;

    public RecuerdoDeTrauma(String nombre, Elemento elemento) {
        super(nombre, true);
        this.elemento = elemento;
        this.daño = 35;
    }

    @Override
    public void activar(Personaje personaje) {

    }
}
