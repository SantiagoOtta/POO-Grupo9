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

        Personaje prueba = new Personaje();
        Dialogos.zona("Prueba");
        Dialogos.actualizarEstado(prueba);
        Dialogos.mostrar("El panel de la derecha arranca vacío: sin recuerdos.");
        prueba.agregarRecuerdo(new RecuerdoDeHabilidad("La tormenta desde la ventana", "Contabas los segundos entre el rayo y el trueno.", Elemento.RAYO));
        prueba.agregarRecuerdo(new RecuerdoDePersona("La voz de mamá", "Te llamaba para entrar a casa."));
        prueba.agregarRecuerdo(new RecuerdoDeLugar("La plaza del barrio", "Hamacas que chirriaban."));
        prueba.agregarRecuerdo(new RecuerdoDeTrauma("La despedida", "Una espalda que se alejaba.", Elemento.AGUA));
        Dialogos.actualizarEstado(prueba);
        Dialogos.mostrar("Ahora tiene 4 recuerdos, uno de cada tipo, con su ícono.");
        Juego juego = new Juego();
        juego.iniciar();
    }
}