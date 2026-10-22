import core.Juego;
import ui.Dialogos;

public class Main {

    public static void main(String[] args) {
        Dialogos.abrir();
        Juego juego = new Juego();
        juego.iniciar();
    }
}