package recuerdos;

import core.Personaje;

public class RecuerdoDePersona extends Recuerdo implements Reutilizable {

    private int usosRestantes;

    public RecuerdoDePersona(String nombre, String descripcion) {
        super(nombre, descripcion);
        this.usosRestantes = 3;
    }

    @Override
    public void activar(Personaje personaje) {
        int porcentaje = usosRestantes * 25;
        personaje.setResistenciaTemporal(personaje.getDebilidad());
        if (porcentaje > personaje.getPorcentajeResistenciaTemporal()) {
            personaje.setPorcentajeResistenciaTemporal(porcentaje);
        }
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