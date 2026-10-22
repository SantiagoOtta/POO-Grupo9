package excepciones;

// Se lanza cuando la vida del jugador llega a 0
// pierde un recuerdo al azar y vuelve al castillo
public class DisolucionException extends Exception {

    public DisolucionException(String mensaje) {
        super(mensaje);
    }
}