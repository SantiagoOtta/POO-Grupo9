package core;

import recuerdos.Recuerdo;

public class Opcion {

    String texto;
    Recuerdo recuerdoRequerido;
    Escena siguienteEscena;

    public Opcion(String texto) {
        this.texto = texto;
    }

    public boolean disponible(Personaje personaje) {
        return false;
    }
}
