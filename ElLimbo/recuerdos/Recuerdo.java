package recuerdos;

import core.Personaje;

public abstract class Recuerdo implements Comparable<Recuerdo> {

    private String nombre;
    private boolean esPermanente;

    public Recuerdo(String nombre, boolean esPermanente) {
        this.nombre = nombre;
        this.esPermanente = esPermanente;
    }

    public abstract void activar(Personaje personaje);

    @Override
    public int compareTo(Recuerdo otro) {
    return 0;
    }
}
