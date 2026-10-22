package excepciones;

// Se lanza cuando el jugador pierde todos sus recuerdos
// muerte definitiva
public class RecuerdoPerdidoException extends Exception {

    public RecuerdoPerdidoException(String mensaje) {
        super(mensaje);
    }
}