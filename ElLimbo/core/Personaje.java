package core;

import recuerdos.Recuerdo;
import excepciones.RecuerdoPerdidoException;
import excepciones.DisolucionException;
import java.util.ArrayList;
import java.util.HashMap;

public class Personaje {

    private int vida;
    private int vidaMaxima;
    private int monedas;
    private ArrayList<Recuerdo> recuerdos;
    private Elemento debilidad;
    private Elemento resistenciaTemporal;
    private HashMap<Elemento, Integer> resistencias;
    private boolean tieneLlave;
    private int bonusDaño;

    public Personaje() {
        this(100);
    }

    public Personaje(int vidaInicial) {
        this.vida = vidaInicial;
        this.vidaMaxima = vidaInicial;
        this.monedas = 0;
        this.recuerdos = new ArrayList<>();
        this.resistencias = new HashMap<>();
        this.tieneLlave = false;
        this.bonusDaño = 0;
    }

    public void recibirDaño(int cantidad, Elemento elemento) {

    }

    public void disolver() throws DisolucionException {

    }

    public void usarRecuerdo(Recuerdo recuerdo) throws RecuerdoPerdidoException {

    }

    public void reiniciarResistencia() {

    }
}
