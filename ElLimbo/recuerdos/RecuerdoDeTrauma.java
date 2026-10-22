package recuerdos;

import core.Elemento;
import core.Personaje;

public class RecuerdoDeTrauma extends Recuerdo {

    private Elemento elemento;
    private int daño;

    public RecuerdoDeTrauma(String nombre, String descripcion, Elemento elemento) {
        super(nombre, descripcion);
        this.elemento = elemento;
        this.daño = 0;
    }

    @Override
    public void activar(Personaje personaje) {}
}