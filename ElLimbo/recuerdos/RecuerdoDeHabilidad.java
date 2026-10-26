package recuerdos;

import core.Elemento;
import core.Personaje;

public class RecuerdoDeHabilidad extends Recuerdo implements Reutilizable {

    private int usosRestantes;
    private boolean fragmentado;
    private Elemento elemento;
    private int daño;

    public RecuerdoDeHabilidad(String nombre, String descripcion, Elemento elemento) {
        super(nombre, descripcion);
        this.elemento = elemento;
        this.usosRestantes = 3;
        this.fragmentado = false;
        this.daño = 14 + (int) (Math.random() * 7);
    }

    public Elemento getElemento() {
        return elemento;
    }

    @Override
    public void activar(Personaje personaje) {

    }

    @Override
    public boolean quedanUsos() {
        return false;
    }

    @Override
    public int getUsosRestantes() {
        return 0;
    }

    @Override
    public void setUsosRestantes(int usosRestantes) {

    }
}