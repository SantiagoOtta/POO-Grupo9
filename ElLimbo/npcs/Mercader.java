package npcs;

import core.Interactuable;
import core.Personaje;
import recuerdos.Recuerdo;
import recuerdos.RecuerdoDeHabilidad;
import recuerdos.RecuerdoDeLugar;
import recuerdos.RecuerdoDePersona;
import recuerdos.RecuerdoDeTrauma;
import recuerdos.Reutilizable;
import ui.Dialogos;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Mercader implements Interactuable {

    private Recuerdo[] stock;

    public Mercader() {
        this.stock = new Recuerdo[3];
    }

    public Mercader(Recuerdo[] stock) {
        this.stock = stock;
    }
    @Override
    public boolean interactuar(Personaje personaje) {
        Dialogos.zona("El Mercader");
        String texto = "Bajo un árbol seco, un viejo encorvado extiende una manta en el suelo."
                + "Encima hay burbujas de luz, ordenadas con cuidado.\n\n"
                + "\"Recuerdos ajenos, recuerdos perdidos... Todo tiene su precio.\"";
        while (true) {
            Dialogos.actualizarEstado(personaje);
            String[] botones = new String[stock.length + 2];
            String[] ayudas = new String[stock.length + 2];
            for (int i = 0; i < stock.length; i++) {
                botones[i] = textoDelPuesto(stock[i], personaje);
                if (stock[i] != null) {
                    ayudas[i] = Dialogos.textoAyuda(personaje, stock[i], 3);
                }
            }
            botones[stock.length] = "Recargar un recuerdo";
            botones[stock.length + 1] = "Seguir camino";

            int elegida = Dialogos.elegir(texto + "\n\nTenés " + personaje.getMonedas() + " monedas.", botones, ayudas);
            if (elegida == stock.length + 1) {
                Dialogos.mostrar("\"Volvé cuando quieras... si todavía recordás el camino.\"");
                return true;
            }
            if (elegida == stock.length) {
                texto = elegirRecarga(personaje);
            } else {
                texto = intentarCompra(stock[elegida], personaje);
            }
        }
    }

    public boolean comprar(Recuerdo recuerdo, Personaje personaje) {
        int posicion = posicionEnStock(recuerdo);
        if (posicion == -1 || personaje.getMonedas() < precio(recuerdo)) {
            return false;
        }
        personaje.setMonedas(personaje.getMonedas() - precio(recuerdo));
        personaje.agregarRecuerdo(recuerdo);
        stock[posicion] = null;
        return true;
    }

    public boolean recargar(Reutilizable recuerdo, Personaje personaje) {
        int costo = costoDeRecarga(recuerdo);
        if (costo == 0 || personaje.getMonedas() < costo) {
            return false;
        }
        personaje.setMonedas(personaje.getMonedas() - costo);
        recuerdo.setUsosRestantes(3);
        return true;
    }

    private String intentarCompra(Recuerdo recuerdo, Personaje personaje) {
        if (recuerdo == null) {
            return "\"Ese ya lo vendí. ¿Algo más?\"";
        }
        if (tiene(personaje, recuerdo)) {
            return "\"Ese ya lo llevás encima. No te lo voy a vender dos veces.\"";
        }
        int bonusAntes = personaje.getBonusDaño();
        if (!comprar(recuerdo, personaje)) {
            return "\"No te alcanza. Ese cuesta " + precio(recuerdo) + "monedas.\"";
        }
        String texto = "Le das las monedas y la burbuja se deshace en tus manos. Ahora " + recuerdo.getNombre() + " es tuyo.";
        if (personaje.getBonusDaño() > bonusAntes) {
            texto = texto + "\n\nCon cada recuerdo que juntás te sentis más entero: +5 de vida máxima y +2 de daño.";
        }
        return texto + "\n\n\"Buena elección. ¿Algo más?\"";
    }

    private String elegirRecarga(Personaje personaje) {
        ArrayList<Recuerdo> gastados = new ArrayList<>();
        for (Recuerdo recuerdo : personaje.getRecuerdos()) {
            if (recuerdo instanceof  Reutilizable && costoDeRecarga((Reutilizable) recuerdo) > 0) {
                gastados.add(recuerdo);
            }
        }
        if (gastados.isEmpty()) {
            return "\"Tus recuerdos están enteros. No hay nada que recargar.\"";
        }

        String[] botones = new String[gastados.size() + 1];
        String[] ayudas = new String[gastados.size() + 1];
        for (int i = 0; i < gastados.size(); i++) {
            Recuerdo recuerdo = gastados.get(i);
            Reutilizable usos = (Reutilizable) recuerdo;
            botones[i] = recuerdo.getNombre() + " · " + usos.getUsosRestantes() + "/3 usos · "
                    + costoDeRecarga(usos) + " monedas";
            ayudas[i] = "Recargado: " + Dialogos.textoAyuda(personaje, recuerdo, 3);
        }
        botones[gastados.size()] = "<- Volver";

        int elegido = Dialogos.elegir("\"¿Cuál querés que te arregle? Cobro 5 monedas por cada uso perdido.\"\n\n"
                + "Tenés " + personaje.getMonedas() + " monedas.", botones, ayudas);
        if (elegido == gastados.size()) {
            return "\"¿Algo más?\"";
        }
        Recuerdo recuerdo = gastados.get(elegido);
        int costo = costoDeRecarga((Reutilizable) recuerdo);
        if (!recargar((Reutilizable) recuerdo, personaje)) {
            return "\"No te alcanza. Arreglar ese cuesta " + costo + " monedas.\"";
        }
        return "El Mercader sopla sobre la burbuja y la luz vuelve a llenarla. " + recuerdo.getNombre()
                + " está entero otra vez: 3/3 usos.\n\n\"¿Algo más?\"";
    }

    private String textoDelPuesto(Recuerdo recuerdo, Personaje personaje) {
        if (recuerdo == null) {
            return "(vendido)";
        }
        String texto = icono(recuerdo) + " " + recuerdo.getNombre();
        if (tiene(personaje, recuerdo)) {
            return texto + " (ya lo tenés)";
        }
        return texto + " · " + precio(recuerdo) + " monedas";
    }

    private int precio(Recuerdo recuerdo) {
        if (recuerdo instanceof RecuerdoDeTrauma) {
            return 35;
        }
        return 20;
    }

    private int costoDeRecarga(Reutilizable recuerdo) {
        return (3 - recuerdo.getUsosRestantes()) * 5;
    }

    private int posicionEnStock(Recuerdo recuerdo) {
        for (int i = 0; i < stock.length; i++) {
            if (stock[i] != null && stock[i] == recuerdo) {
                return i;
            }
        }
        return -1;
    }

    private boolean tiene(Personaje personaje, Recuerdo buscado) {
        for (Recuerdo recuerdo : personaje.getRecuerdos()) {
            if (recuerdo.getNombre().equals(buscado.getNombre())) {
                return true;
            }
        }
        return false;
    }

    private String icono(Recuerdo recuerdo) {
        if (recuerdo instanceof RecuerdoDeHabilidad) {
            return "⚔";
        } else if (recuerdo instanceof RecuerdoDePersona) {
            return "♥";
        } else if (recuerdo instanceof RecuerdoDeLugar) {
            return "⌂";
        }
        return "✶";
    }
}