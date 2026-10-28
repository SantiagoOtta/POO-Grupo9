package recuerdos;

import core.Elemento;
import core.Personaje;

public class RecuerdoDeHabilidad extends Recuerdo implements Reutilizable {

    private int usosRestantes;
    private boolean fragmentado;
    private Elemento elemento;
    private int daño;
    private boolean enArea;

    public RecuerdoDeHabilidad(String nombre, String descripcion, Elemento elemento, boolean enArea) {
        super(nombre, descripcion);
        this.elemento = elemento;
        this.enArea = enArea;
        this.usosRestantes = 3;
        this.fragmentado = false;
        if (enArea) {
            this.daño = 10 + (int) (Math.random() * 5);
        } else {
            this.daño = 14 + (int) (Math.random() * 7);
        }
    }

    public Elemento getElemento() {
        return elemento;
    }

    public int getDaño() {
        return daño;
    }

    public boolean isFragmentado() {
        return fragmentado;
    }

    public boolean isEnArea() {
        return enArea;
    }

    @Override
    protected int dañoDeAtaque() {
        return daño;
    }

    @Override
    public void activar(Personaje personaje) {
        fragmentado = (usosRestantes == 1);
        usosRestantes--;
    }

    @Override
    public boolean quedanUsos() {
        return usosRestantes > 0;
    }

    @Override
    public int getUsosRestantes() {
        return usosRestantes;
    }

    @Override
    public void setUsosRestantes(int usosRestantes) {
        this.usosRestantes = usosRestantes;
        this.fragmentado = false;
    }
}