package recuerdos;

import core.Personaje;

public class RecuerdoDeLugar extends Recuerdo implements Reutilizable {

    private int usosRestantes;

    public RecuerdoDeLugar(String nombre, String descripcion) {
        super(nombre, descripcion);
        this.usosRestantes = 3;
    }

    @Override
    public void activar(Personaje personaje) {
        int porcentaje = usosRestantes * 25;
        int cura = personaje.getVidaMaxima() * porcentaje / 100;
        int vidaNueva = Math.min(personaje.getVidaMaxima(), personaje.getVida() + cura);
        personaje.setVida(vidaNueva);
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
    }
}