package recuerdos;

import core.Elemento;
import core.Personaje;

public class RecuerdoDeHabilidad extends Recuerdo implements Reutilizable{
    private int usosRestantes;
    private boolean fragmentado;
    private Elemento elemento;
    private int daño;

    public RecuerdoDeHabilidad(String nombre, Elemento elemento) {
        super(nombre, false);
        this.elemento = elemento;
        this.usosRestantes = 3;
        this.fragmentado = false;
        this.daño = 15;
    }

    @Override
    public void activar(Personaje personaje) {

    }

    @Override
    public boolean quedanUsos() {
        return false;
    }
}