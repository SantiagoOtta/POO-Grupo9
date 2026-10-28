package core;

import recuerdos.Recuerdo;

public class Opcion {

    String texto;
    Recuerdo recuerdoRequerido;
    Escena siguienteEscena;

    public Opcion(String texto) {
        this(texto, null, null);
    }

    public Opcion(String texto, Recuerdo recuerdoRequerido, Escena siguienteEscena) {
        this.texto = texto;
        this.recuerdoRequerido = recuerdoRequerido;
        this.siguienteEscena = siguienteEscena;
    }

    public String getTexto() {
        return texto;
    }

    public Escena getSiguienteEscena() {
        return siguienteEscena;
    }

    public boolean disponible(Personaje personaje) {
        if (recuerdoRequerido == null) {
            return true;
        }
        for (Recuerdo recuerdo : personaje.getRecuerdos()) {
            if (recuerdo.getNombre().equals(recuerdoRequerido.getNombre())) {
                return true;
            }
        }
        return false;
    }
}