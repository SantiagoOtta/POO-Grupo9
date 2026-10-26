import core.Elemento;
import core.Juego;
import core.Personaje;
import recuerdos.RecuerdoDeHabilidad;
import recuerdos.RecuerdoDeLugar;
import recuerdos.RecuerdoDePersona;
import recuerdos.RecuerdoDeTrauma;
import ui.Dialogos;

public class Main {

    public static void main(String[] args) {
        Dialogos.abrir();
        Juego juego = new Juego();
        juego.iniciar();
    }
}