package recuerdos;

import core.Personaje;

public abstract class Recuerdo implements Comparable<Recuerdo> {

    private String nombre;
    private String descripcion;

    public Recuerdo(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public abstract void activar(Personaje personaje);


    protected int dañoDeAtaque() {
        return 0;
    }

    @Override
    public int compareTo(Recuerdo otro) {
        return otro.dañoDeAtaque() - this.dañoDeAtaque();
    }
}