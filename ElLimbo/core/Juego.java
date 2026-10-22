package core;

import npcs.Mercader;

public class Juego {

    private Personaje jugador;
    private Escena escenaActual;
    private int puertasReveladas;
    private int encuentrosMercader;
    private Mercader mercader;

    public Juego() {
        this.jugador = new Personaje();
        this.escenaActual = null;
        this.puertasReveladas = 0;
        this.encuentrosMercader = 0;
        this.mercader = null;
    }

    public void iniciar() {

    }

    public void otorgarRecompensa(Personaje personaje) {

    }

    public Escena generarBosque() {
        return null;
    }

    public void guardarPartida() {

    }

    public boolean cargarPartida() {
        return false;
    }
}