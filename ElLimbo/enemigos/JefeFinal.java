package enemigos;

import core.Elemento;
import core.Personaje;

public class JefeFinal extends Enemigo {

    private int faseActual;
    private int cantidadFases;
    private int turnosJugados;

    public JefeFinal(Elemento elemento, int vida, Elemento debilidad, int daño, int cantidadFases) {
        super(elemento, vida, debilidad, daño);
        this.cantidadFases = cantidadFases;
        this.faseActual = 1;
        this.turnosJugados = 0;
    }

    public int getFaseActual() {
        return faseActual;
    }

    public int getTurnosJugados() {
        return turnosJugados;
    }

    @Override
    public void infligirDaño(Personaje personaje) {
        turnosJugados ++;
        if (turnosJugados % 3 == 0) {
            ataqueEspecial(personaje);
        } else {
            personaje.recibirDaño(daño + (int) (Math.random() * 11), elemento);
        }
    }

    public void ataqueEspecial(Personaje personaje) {
        personaje.recibirDaño(20 + (int) (Math.random() * 21), elemento);
    }

    public void avanzarFase() {
        faseActual ++;
        vidaMaxima = 125;
        vida = vidaMaxima;
    }

    @Override
    public void recibirDaño(int cantidad, Elemento elementoAtaque) {
        super.recibirDaño(cantidad, elementoAtaque);
        if (vida <= 0 && faseActual < cantidadFases) {
            avanzarFase();
        }
    }
}