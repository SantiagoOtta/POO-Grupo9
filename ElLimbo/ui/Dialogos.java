package ui;

import core.Elemento;
import core.Personaje;
import recuerdos.Recuerdo;
import recuerdos.RecuerdoDeHabilidad;
import recuerdos.RecuerdoDeLugar;
import recuerdos.RecuerdoDePersona;

import javax.swing.SwingUtilities;
import java.util.concurrent.ArrayBlockingQueue;

public class Dialogos {

    private static VentanaJuego ventana;
    private static final ArrayBlockingQueue<Integer> respuestas = new ArrayBlockingQueue<>(1);

    public static void abrir() {
        ventana = new VentanaJuego();
        ventana.setVisible(true);
    }

    public static void cerrar() {
        SwingUtilities.invokeLater(() -> ventana.dispose());
    }

    public static void zona(String titulo) {
        SwingUtilities.invokeLater(() -> ventana.ponerZona(titulo));
    }

    public static void mostrar(String texto) {
        preguntar(texto, new String[]{"Continuar"});
    }

    public static int elegir(String texto, String[] opciones) {
        return preguntar(texto, opciones);
    }

    public static boolean confirmar(String texto) {
        return preguntar(texto, new String[]{"Sí", "No"}) == 0;
    }

    public static void actualizarEstado(Personaje personaje) {
        int vida = personaje.getVida();
        int vidaMaxima = personaje.getVidaMaxima();

        String datos = "<html>Monedas: " + personaje.getMonedas()
                + "<br>Bonus de daño: +" + personaje.getBonusDaño()
                + "<br>Debilidad: " + nombreElemento(personaje.getDebilidad())
                + "<br>Llave del castillo: " + (personaje.isTieneLlave() ? "sí" : "no")
                + "<br>Recuerdo clave: " + (personaje.isTieneRecuerdoClave() ? "sí" : "no")
                + "</html>";

        StringBuilder lista = new StringBuilder("<html><div style='width:180px'>");
        for (Recuerdo recuerdo : personaje.getRecuerdos()) {
            lista.append(icono(recuerdo)).append(" ").append(recuerdo.getNombre()).append("<br>");
        }
        lista.append("</div></html>");
        String recuerdos = lista.toString();
        int cantidad = personaje.getRecuerdos().size();

        SwingUtilities.invokeLater(() -> ventana.getPanelEstado().ponerEstado(vida, vidaMaxima, datos, recuerdos, cantidad));
    }

    public static void permitirGuardar(boolean permitido) {
        SwingUtilities.invokeLater(() -> ventana.getPanelEstado().permitirGuardar(permitido));
    }

    public static void alGuardar(Runnable accion) {
        SwingUtilities.invokeLater(() -> ventana.getPanelEstado().setAccionGuardar(accion));
    }

    public static void avisar(String texto) {
        SwingUtilities.invokeLater(() -> ventana.getPanelEstado().ponerAviso(texto));
    }

    private static int preguntar(String texto, String[] opciones) {
        respuestas.clear();
        SwingUtilities.invokeLater(() -> ventana.ponerPregunta(texto, opciones, respuestas));
        try {
            return respuestas.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return 0;
        }
    }

    private static String icono(Recuerdo recuerdo) {
        if (recuerdo instanceof RecuerdoDeHabilidad) {
            return "⚔";
        } else if (recuerdo instanceof RecuerdoDePersona) {
            return "♥";
        } else if (recuerdo instanceof RecuerdoDeLugar) {
            return "⌂";
        } else {
            return "✶";
        }
    }

    public static String nombreElemento(Elemento elemento) {
        if (elemento == null) {
            return "ninguna";
        }
        String nombre = elemento.name().toLowerCase();
        return nombre.substring(0, 1).toUpperCase() + nombre.substring(1);
    }
}