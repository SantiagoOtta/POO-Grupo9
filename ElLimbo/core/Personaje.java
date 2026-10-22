package core;

import excepciones.DisolucionException;
import excepciones.RecuerdoPerdidoException;
import recuerdos.Recuerdo;
import java.util.ArrayList;
import java.util.HashMap;

public class Personaje {

    private int vida;
    private int vidaMaxima;
    private int monedas;
    private ArrayList<Recuerdo> recuerdos;
    private Elemento debilidad;
    private Elemento resistenciaTemporal;
    private int porcenntajeResistenciaTemporal;
    private HashMap<Elemento, Integer> resistencias;
    private boolean tieneLlave;
    private boolean tieneRecuerdoClave;
    private int bonusDaño;

    public Personaje() {
        this(100);
    }

    public Personaje(int vidaInicial) {
        this.vida = vidaInicial;
        this.vidaMaxima = vidaInicial;
        this.monedas = 0;
        this.recuerdos = new ArrayList<>();
        this.debilidad = null;
        this.resistenciaTemporal = null;
        this.porcenntajeResistenciaTemporal = 0;
        this.resistencias = new HashMap<>();
        this.tieneLlave = false;
        this.tieneRecuerdoClave = false;
        this.bonusDaño = 0;
    }

    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getMonedas() {
        return monedas;
    }

    public int getBonusDaño() {
        return bonusDaño;
    }

    public Elemento getDebilidad() {
        return debilidad;
    }

    public boolean isTieneLlave() {
        return tieneLlave;
    }

    public ArrayList<Recuerdo> getRecuerdos() {
        return new ArrayList<>(recuerdos);
    }

    public void agregarRecuerdo(Recuerdo recuerdo) {
        recuerdos.add(recuerdo);
    }

    public void recibirDaño(int cantidad, Elemento elemento) {

    }

    public void disolver() throws DisolucionException {

    }

    public void usarRecuerdo(Recuerdo recuerdo) throws RecuerdoPerdidoException {

    }

    public void reiniciarResistencias() {

    }
}