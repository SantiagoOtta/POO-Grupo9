package recuerdos;

import core.Personaje;

public class RecuerdoDePersona extends Recuerdo implements Reutilizable {

    private int usosRestantes;

    public RecuerdoDePersona(String nombre) {
        super(nombre, false);
        this.usosRestantes = 3;
    }

    @Override
    public void activar(Personaje personaje) {

    }

    @Override
    public boolean quedanUsos() {
        return false;
    }
}
