package core;

import excepciones.RecuerdoPerdidoException;
import recuerdos.Recuerdo;
import recuerdos.RecuerdoDeLugar;
import recuerdos.Reutilizable;
import ui.Dialogos;
import java.util.ArrayList;

public class Escena {

    private ArrayList<Opcion> opciones;
    private Interactuable interactuable;

    public Escena(ArrayList<Opcion> opciones, Interactuable interactuable) {
        this.opciones = opciones;
        this.interactuable = interactuable;
    }

    public boolean resolver(Personaje personaje) {
        if (interactuable != null && !interactuable.interactuar(personaje)) {
            return false;
        }
        Opcion elegida = elegirCamino(personaje);
        if (elegida.getSiguienteEscena() == null) {
            return true;
        }
        return elegida.getSiguienteEscena().resolver(personaje);
    }

    private Opcion elegirCamino(Personaje personaje) {
        while (true) {
            Dialogos.zona("El bosque");
            Dialogos.actualizarEstado(personaje);

            ArrayList<Opcion> disponibles = new ArrayList<>();
            ArrayList<String> botones = new ArrayList<>();
            for (Opcion opcion : opciones) {
                if (opcion.disponible(personaje)) {
                    disponibles.add(opcion);
                    botones.add(opcion.getTexto());
                }
            }
            ArrayList<Recuerdo> lugares = lugaresConUsos(personaje);
            if (!lugares.isEmpty() && personaje.getVida() < personaje.getVidaMaxima()) {
                botones.add("Curarme con un recuerdo de lugar");
            }

            int elegida = Dialogos.elegir(textoDelCamino(disponibles), botones.toArray(new String[0]));
            if (elegida < disponibles.size()) {
                return disponibles.get(elegida);
            }
            curarse(personaje, lugares);
        }
    }

    private String textoDelCamino(ArrayList<Opcion> disponibles) {
        if (disponibles.size() == 1 && disponibles.get(0).getSiguienteEscena() == null) {
            return "Los árboles se abren. Al final del sendero se recorta el castillo.";
        }
        if (disponibles.size() == 1) {
            return "Entre los árboles hay un solo sendero.";
        }
        return "El sendero se abre en " + disponibles.size() + " caminos. ¿Por dónde seguís?";
    }

    private ArrayList<Recuerdo> lugaresConUsos(Personaje personaje) {
        ArrayList<Recuerdo> lugares = new ArrayList<>();
        for (Recuerdo recuerdo : personaje.getRecuerdos()) {
            if (recuerdo instanceof RecuerdoDeLugar && ((Reutilizable) recuerdo).quedanUsos()) {
                lugares.add(recuerdo);
            }
        }
        return lugares;
    }

    private void curarse(Personaje personaje, ArrayList<Recuerdo> lugares) {
        Recuerdo lugar = lugares.get(0);
        if (lugares.size() > 1) {
            String[] botones = new String[lugares.size()];
            for (int i = 0; i < lugares.size(); i++) {
                Reutilizable usos = (Reutilizable) lugares.get(i);
                botones[i] = lugares.get(i).getNombre() + " · cura " + usos.getUsosRestantes() * 25 + "% · "
                        + usos.getUsosRestantes() + "/3 usos";
            }
            lugar = lugares.get(Dialogos.elegir("¿Qué lugar recordás?", botones));
        }
        int vidaAntes = personaje.getVida();
        try {
            personaje.usarRecuerdo(lugar);
        } catch (RecuerdoPerdidoException e) {

        }
        Dialogos.actualizarEstado(personaje);
        Dialogos.mostrar("Cerrás los ojos y volvés a " + lugar.getNombre() + ". Recuperás "
                + (personaje.getVida() - vidaAntes) +  " de vida");
    }
}