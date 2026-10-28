package enemigos;

import core.Elemento;
import core.Interactuable;
import core.Personaje;

import java.util.ArrayList;

public abstract class Enemigo implements Interactuable {

    protected Elemento elemento;
    protected int vida;
    protected int vidaMaxima;
    protected Elemento debilidad;
    protected int daño;

    public Enemigo(Elemento elemento, int vida, Elemento debilidad, int daño) {
        this.elemento = elemento;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.debilidad = debilidad;
        this.daño = daño;
    }

    public Elemento getElemento() {
        return elemento;
    }

    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public Elemento getDebilidad() {
        return debilidad;
    }

    @Override
    public boolean interactuar(Personaje personaje) {
        ArrayList<Enemigo> soloEste = new ArrayList<>();
        soloEste.add(this);
        GrupoDeEnemigos grupo = new GrupoDeEnemigos(soloEste);
        return grupo.interactuar(personaje);
    }

    public abstract void infligirDaño(Personaje personaje);

    public void recibirDaño(int cantidad, Elemento elementoAtaque) {
        if (elementoAtaque != null && elementoAtaque == debilidad) {
            cantidad = cantidad * 3 / 2;
        }
        vida = vida - cantidad;
        if (vida < 0) {
            vida = 0;
        }
    }

    public boolean sigueEnCombate() {
        return vida > 0;
    }
}