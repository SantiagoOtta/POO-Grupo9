package recuerdos;

import core.Personaje;

public class RecuerdoDeLugar extends Recuerdo implements Reutilizable {

    private int usosRestantes;

    public RecuerdoDeLugar(String nombre) {
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
