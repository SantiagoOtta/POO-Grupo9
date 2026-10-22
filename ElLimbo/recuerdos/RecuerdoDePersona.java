package recuerdos;

import core.Personaje;

public class RecuerdoDePersona extends Recuerdo implements Reutilizable {

    public int usosRestantes;

    public RecuerdoDePersona(String nombre, String descripcion) {
        super(nombre, descripcion);
        this.usosRestantes = 3;
    }

    @Override
    public void activar (Personaje personaje) {

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
    public void setUsosRestantes(int usosRestantes) {}
}