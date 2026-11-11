package npcs;

import core.Elemento;
import core.Interactuable;
import core.Personaje;
import recuerdos.Recuerdo;
import recuerdos.RecuerdoDeHabilidad;
import recuerdos.RecuerdoDeLugar;
import recuerdos.RecuerdoDePersona;
import recuerdos.RecuerdoDeTrauma;
import recuerdos.Reutilizable;
import ui.Dialogos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class GuardianDeRecuerdos implements Interactuable {

    private static HashMap<Personaje, ArrayList<Recuerdo>> memoriaGlobal = new HashMap<>();

    static {
        cargarArchivo();
    }

    @Override
    public boolean interactuar(Personaje personaje) {
        Dialogos.zona("El Guardián de Recuerdos");
        String texto = "Entre las raíces de un árbol enorme hay un cofre de piedra. Encima, sentada, una figura sin cara "
                + "te espera con las manos sobre las rodillas.\n\n"
                + "\"Lo que me dejes lo voy a cuidar más allá de esta vida. No para vos: para el que venga después.\"";
        boolean dejaste = false;
        boolean llevaste = false;
        while (true) {
            Dialogos.actualizarEstado(personaje);
            ArrayList<Recuerdo> disponibles = disponiblesPara(personaje);
            ArrayList<String> opciones = new ArrayList<>();
            if (!dejaste && personaje.getRecuerdos().size() > 1) {
                opciones.add("Dejarle un recuerdo");
            }
            if (!llevaste && !disponibles.isEmpty()) {
                String guardados = disponibles.size() == 1 ? "1 guardado" : disponibles.size() + " guardados";
                opciones.add("Llevarte un recuerdo (" + guardados + ")");
            }
            opciones.add("Seguir camino");

            String opcion = opciones.get(Dialogos.elegir(texto, opciones.toArray(new String[0])));
            if (opcion.equals("Seguir camino")) {
                return true;
            }
            if (opcion.equals("Dejarle un recuerdo")) {
                Recuerdo elegido = elegirDe(personaje.getRecuerdos(), personaje,
                        "\"¿Cuál me dejás? Mientras lo tenga yo, no lo vas a poder usar.\"");
                if (elegido != null) {
                    archivar(elegido, personaje);
                    dejaste = true;
                    texto = "El Guardián toma " + elegido.getNombre() + " y lo guarda en el cofre. "
                            + "Sentís un hueco donde estaba.\n\n\"Alguien lo va a necesitar.\"";
                } else {
                    texto = "\"Tomate tu tiempo.\"";
                }
            } else {
                Recuerdo elegido = elegirDe(disponibles, personaje,
                        "\"Estos los dejaron otros como vos. Podés llevarte uno.\"");
                if (elegido != null) {
                    personaje.agregarRecuerdo(reclamar(elegido));
                    llevaste = true;
                    texto = "El Guardián abre el cofre y te da " + elegido.getNombre()
                            + ". Está entero, como si nadie lo hubiera usado nunca.";
                } else {
                    texto = "\"Tomate tu tiempo.\"";
                }
            }
        }
    }

    public void archivar(Recuerdo recuerdo, Personaje personaje) {
        personaje.quitarRecuerdo(recuerdo);
        synchronized (memoriaGlobal) {
            if (!memoriaGlobal.containsKey(personaje)) {
                memoriaGlobal.put(personaje, new ArrayList<>());
            }
            memoriaGlobal.get(personaje).add(recuerdo);
        }
        guardarEnArchivo();
    }

    public Recuerdo reclamar(Recuerdo recuerdo) {
        boolean encontrado = false;
        synchronized (memoriaGlobal) {
            ArrayList<ArrayList<Recuerdo>> listas = new ArrayList<>(memoriaGlobal.values());
            int i = 0;
            while (!encontrado && i < listas.size()) {
                encontrado = listas.get(i).remove(recuerdo);
                i++;
            }
        }
        if (!encontrado) {
            return null;
        }
        if (recuerdo instanceof Reutilizable) {
            ((Reutilizable) recuerdo).setUsosRestantes(3);
        }
        guardarEnArchivo();
        return recuerdo;
    }

    public void guardarEnArchivo() {
        Thread hilo = new Thread(() -> escribir());
        hilo.start();
    }

    private static void escribir() {
        synchronized (memoriaGlobal) {
            try (PrintWriter salida = new PrintWriter(new FileWriter("memoria_global.txt"))) {
                for (ArrayList<Recuerdo> guardados : memoriaGlobal.values()) {
                    for (Recuerdo recuerdo : guardados) {
                        salida.println(aLinea(recuerdo));
                    }
                }
            } catch (IOException e) {
                Dialogos.avisar("No se pudo guardar el cofre del Guardián.");
            }
        }
    }

    private static void cargarArchivo() {
        ArrayList<Recuerdo> cargados = new ArrayList<>();
        int dañadas = 0;
        try (BufferedReader entrada = new BufferedReader(new FileReader("memoria_global.txt"))) {
            String linea = entrada.readLine();
            while (linea != null) {
                try {
                    Recuerdo recuerdo = deLinea(linea);
                    if (recuerdo == null) {
                        dañadas++;
                    } else {
                        cargados.add(recuerdo);
                    }
                } catch (IllegalArgumentException e) {
                    dañadas++;
                } finally {
                    linea = entrada.readLine();
                }
            }
        } catch (IOException e) {
            return;
        }
        memoriaGlobal.put(null, cargados);
        if (dañadas > 0) {
            escribir();
        }
    }

    private ArrayList<Recuerdo> disponiblesPara(Personaje personaje) {
        ArrayList<Recuerdo> disponibles = new ArrayList<>();
        synchronized (memoriaGlobal) {
            for (Personaje dueño : memoriaGlobal.keySet()) {
                if (dueño != personaje) {
                    for (Recuerdo recuerdo : memoriaGlobal.get(dueño)) {
                        if (!tiene(personaje, recuerdo)) {
                            disponibles.add(recuerdo);
                        }
                    }
                }
            }
        }
        return disponibles;
    }

    private Recuerdo elegirDe(ArrayList<Recuerdo> recuerdos, Personaje personaje, String texto) {
        String[] botones = new String[recuerdos.size() + 1];
        String[] ayudas = new String[recuerdos.size() + 1];
        for (int i = 0; i < recuerdos.size(); i++) {
            botones[i] = recuerdos.get(i).getNombre();
            ayudas[i] = Dialogos.textoAyuda(personaje, recuerdos.get(i), 3);
        }
        botones[recuerdos.size()] = "← Volver";
        int elegido = Dialogos.elegir(texto, botones, ayudas);
        if (elegido == recuerdos.size()) {
            return null;
        }
        return recuerdos.get(elegido);
    }

    private boolean tiene(Personaje personaje, Recuerdo buscado) {
        for (Recuerdo recuerdo : personaje.getRecuerdos()) {
            if (recuerdo.getNombre().equals(buscado.getNombre())) {
                return true;
            }
        }
        return false;
    }

    private static String aLinea(Recuerdo recuerdo) {
        String tipo = "LUGAR";
        String elemento = "-";
        String enArea = "-";
        if (recuerdo instanceof RecuerdoDeHabilidad) {
            RecuerdoDeHabilidad habilidad = (RecuerdoDeHabilidad) recuerdo;
            tipo = "HABILIDAD";
            elemento = habilidad.getElemento().name();
            enArea = String.valueOf(habilidad.isEnArea());
        } else if (recuerdo instanceof RecuerdoDeTrauma) {
            tipo = "TRAUMA";
            elemento = ((RecuerdoDeTrauma) recuerdo).getElemento().name();
        } else if (recuerdo instanceof RecuerdoDePersona) {
            tipo = "PERSONA";
        }
        return tipo + "|" + recuerdo.getNombre() + "|" + recuerdo.getDescripcion() + "|" + elemento + "|" + enArea;
    }

    private static Recuerdo deLinea(String linea) {
        String[] partes = linea.split("\\|");
        if (partes.length != 5) {
            return null;
        }
        String nombre = partes[1];
        String descripcion = partes[2];
        switch (partes[0]) {
            case "HABILIDAD":
                return new RecuerdoDeHabilidad(nombre, descripcion, Elemento.valueOf(partes[3]), Boolean.parseBoolean(partes[4]));
            case "TRAUMA":
                return new RecuerdoDeTrauma(nombre, descripcion, Elemento.valueOf(partes[3]));
            case "PERSONA":
                return new RecuerdoDePersona(nombre, descripcion);
            case "LUGAR":
                return new RecuerdoDeLugar(nombre, descripcion);
            default:
                return null;
        }
    }
}