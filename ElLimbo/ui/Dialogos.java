package ui;

import core.Personaje;

public class Dialogos {

    private static VentanaJuego ventana;

    public static void abrir() {
        ventana = new VentanaJuego();
        ventana.setVisible(true);
    }

    public static void zona(String titulo) {

    }

    public static void mostrar(String texto) {

    }

    public static int elegir(String texto, String[] opciones) {
        return 0;
    }

    public static boolean confirmar(String texto) {
        return false;
    }

    public static void actualizarEstado(Personaje personaje) {

    }
}