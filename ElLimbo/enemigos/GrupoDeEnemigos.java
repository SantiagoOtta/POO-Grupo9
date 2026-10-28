package enemigos;

import core.Elemento;
import core.Interactuable;
import core.Personaje;
import excepciones.RecuerdoPerdidoException;
import recuerdos.Recuerdo;
import recuerdos.RecuerdoDeHabilidad;
import recuerdos.RecuerdoDePersona;
import recuerdos.RecuerdoDeTrauma;
import recuerdos.Reutilizable;
import ui.Dialogos;

import java.util.ArrayList;
import java.util.Collections;

public class GrupoDeEnemigos implements Interactuable {

    private ArrayList<Enemigo> enemigos;

    public GrupoDeEnemigos(ArrayList<Enemigo> enemigos) {
        this.enemigos = enemigos;
    }

    @Override
    public boolean interactuar(Personaje personaje) {
        Dialogos.zona("Combate");
        Dialogos.actualizarEstado(personaje);
        actualizarTarjetas();
        Dialogos.mostrar(presentacion());
        while (personaje.getVida() > 0 && quedanEnemigos() && !personaje.getRecuerdos().isEmpty()) {
            String resumen = turnoDelJugador(personaje);
            actualizarTarjetas();
            resumen = resumen + turnoDeLosEnemigos(personaje);
            Dialogos.actualizarEstado(personaje);
            Dialogos.mostrar(resumen);
        }
        Dialogos.ocultarEnemigos();
        personaje.setResistenciaTemporal(null);
        personaje.setPorcentajeResistenciaTemporal(0);
        return personaje.getVida() > 0 && !personaje.getRecuerdos().isEmpty();
    }

    private String turnoDelJugador(Personaje personaje) {
        ArrayList<Recuerdo> habilidades = new ArrayList<>();
        ArrayList<Recuerdo> personas = new ArrayList<>();
        ArrayList<Recuerdo> lugares = new ArrayList<>();
        ArrayList<Recuerdo> traumas = new ArrayList<>();
        for (Recuerdo recuerdo : personaje.getRecuerdos()) {
            if (recuerdo instanceof RecuerdoDeTrauma) {
                traumas.add(recuerdo);
            } else if (((Reutilizable) recuerdo).quedanUsos()) {
                if (recuerdo instanceof RecuerdoDeHabilidad) {
                    habilidades.add(recuerdo);
                } else if (recuerdo instanceof RecuerdoDePersona) {
                    personas.add(recuerdo);
                } else {
                    lugares.add(recuerdo);
                }
            }
        }

        ArrayList<String> categorias = new ArrayList<>();
        ArrayList<ArrayList<Recuerdo>> listas = new ArrayList<>();
        categorias.add("⚔ Habilidades (" + habilidades.size() + ")");
        listas.add(habilidades);
        agregarCategoria(categorias, listas, "♥ Personas: protegen", personas);
        agregarCategoria(categorias, listas, "⌂ Lugares: curan", lugares);
        agregarCategoria(categorias, listas, "✶ Traumas: uso único, cuestan vida", traumas);

        while (true) {
            int categoria = Dialogos.elegir("Tu turno. ¿Qué hacés?", categorias.toArray(new String[0]));
            ArrayList<Recuerdo> lista = listas.get(categoria);
            Collections.sort(lista);
            boolean conHacha = (categoria == 0);

            int cantidadBotones = lista.size() + 1;
            if (conHacha) {
                cantidadBotones++;
            }
            String[] botones = new String[cantidadBotones];
            String[] ayudas = new String[cantidadBotones];
            for (int i = 0; i < lista.size(); i++) {
                botones[i] = textoBoton(lista.get(i));
                ayudas[i] = textoAyuda(personaje, lista.get(i));
            }
            if (conHacha) {
                botones[lista.size()] = "Golpe de hacha (sin elemento)";
                ayudas[lista.size()] = "Sin elemento · " + (6 + personaje.getBonusDaño()) + " a "
                        + (8 + personaje.getBonusDaño()) + " de daño a un enemigo · no se gasta";
            }
            botones[cantidadBotones - 1] = "← Volver";

            int elegido = Dialogos.elegir("¿Qué usás?", botones, ayudas);

            if (elegido < lista.size()) {
                Recuerdo recuerdo = lista.get(elegido);
                if (recuerdo instanceof RecuerdoDeHabilidad) {
                    return usarHabilidad(personaje, (RecuerdoDeHabilidad) recuerdo);
                } else if (recuerdo instanceof RecuerdoDeTrauma) {
                    return usarTrauma(personaje, (RecuerdoDeTrauma) recuerdo);
                } else if (recuerdo instanceof RecuerdoDePersona) {
                    return usarPersona(personaje, recuerdo);
                } else {
                    return usarLugar(personaje, recuerdo);
                }
            } else if (conHacha && elegido == lista.size()) {
                Enemigo objetivo = elegirObjetivo("Levantás el hacha. ¿A quién le pegás? Tocá su tarjeta.");
                int daño = 6 + (int) (Math.random() * 3) + personaje.getBonusDaño();
                return "Descargás el hacha." + golpear(objetivo, daño, null);
            }
        }
    }

    private void agregarCategoria(ArrayList<String> categorias, ArrayList<ArrayList<Recuerdo>> listas,
                                  String nombre, ArrayList<Recuerdo> recuerdos) {
        if (!recuerdos.isEmpty()) {
            categorias.add(nombre + " (" + recuerdos.size() + ")");
            listas.add(recuerdos);
        }
    }

    private String usarHabilidad(Personaje personaje, RecuerdoDeHabilidad habilidad) {
        Enemigo objetivo = null;
        if (!habilidad.isEnArea()) {
            objetivo = elegirObjetivo("Usás " + habilidad.getNombre() + ". ¿A quién? Tocá su tarjeta.");
        }
        String error = usar(personaje, habilidad);
        if (error != null) {
            return error;
        }

        int daño = habilidad.getDaño();
        if (habilidad.isFragmentado()) {
            daño = daño / 2;
        }
        daño = daño + personaje.getBonusDaño();

        String texto = "Usás " + habilidad.getNombre() + ".";
        if (habilidad.isFragmentado()) {
            texto = texto + " El recuerdo está fragmentado: pega la mitad.";
        }
        if (habilidad.isEnArea()) {
            for (Enemigo enemigo : enemigos) {
                if (enemigo.sigueEnCombate()) {
                    texto = texto + golpear(enemigo, daño, habilidad.getElemento());
                }
            }
        } else {
            texto = texto + golpear(objetivo, daño, habilidad.getElemento());
        }
        return texto;
    }

    private String usarTrauma(Personaje personaje, RecuerdoDeTrauma trauma) {
        Enemigo objetivo = elegirObjetivo("Usás " + trauma.getNombre() + ". ¿A quién? Tocá su tarjeta.");
        int vidaAntes = personaje.getVida();
        String error = usar(personaje, trauma);
        int costo = vidaAntes - personaje.getVida();
        if (error != null) {
            return error;
        }
        int daño = trauma.getDaño() + personaje.getBonusDaño();
        return "Revivís " + trauma.getNombre() + ". El recuerdo te cuesta " + costo + " de vida y se rompe."
                + golpear(objetivo, daño, trauma.getElemento());
    }

    private String usarPersona(Personaje personaje, Recuerdo persona) {
        String error = usar(personaje, persona);
        if (error != null) {
            return error;
        }
        return "Te aferrás a un recuerdo: " + persona.getNombre() + ". Durante este combate, el "
                + Dialogos.nombreElemento(personaje.getResistenciaTemporal()).toLowerCase() + " te hace "
                + personaje.getPorcentajeResistenciaTemporal() + "% menos de daño.";
    }

    private String usarLugar(Personaje personaje, Recuerdo lugar) {
        int vidaAntes = personaje.getVida();
        String error = usar(personaje, lugar);
        if (error != null) {
            return error;
        }
        int curado = personaje.getVida() - vidaAntes;
        return "Volvés por un momento a un lugar: " + lugar.getNombre() + ". Recuperás " + curado + " de vida.";
    }

    private String usar(Personaje personaje, Recuerdo recuerdo) {
        try {
            personaje.usarRecuerdo(recuerdo);
            return null;
        } catch (RecuerdoPerdidoException e) {
            return e.getMessage();
        }
    }

    private String textoBoton(Recuerdo recuerdo) {
        if (recuerdo instanceof RecuerdoDeHabilidad) {
            RecuerdoDeHabilidad habilidad = (RecuerdoDeHabilidad) recuerdo;
            String alcance = habilidad.isEnArea() ? ", a todos" : "";
            return habilidad.getNombre() + " (" + Dialogos.nombreElemento(habilidad.getElemento()) + alcance + ") · "
                    + textoUsos(habilidad);
        } else if (recuerdo instanceof RecuerdoDeTrauma) {
            RecuerdoDeTrauma trauma = (RecuerdoDeTrauma) recuerdo;
            return trauma.getNombre() + " (" + Dialogos.nombreElemento(trauma.getElemento()) + ") · uso único";
        } else {
            return recuerdo.getNombre() + " · " + textoUsos((Reutilizable) recuerdo);
        }
    }

    // Lo que se lee al pasar el mouse sobre el botón: qué hace el recuerdo si lo usás ahora
    private String textoAyuda(Personaje personaje, Recuerdo recuerdo) {
        int usos = 1;
        if (recuerdo instanceof Reutilizable) {
            usos = ((Reutilizable) recuerdo).getUsosRestantes();
        }
        return textoAyuda(personaje, recuerdo, usos);
    }

    // Lo mismo, pero contando con una cantidad de usos dada (al recargar, el recuerdo vuelve a tener 3)
    public static String textoAyuda(Personaje personaje, Recuerdo recuerdo, int usos) {
        String cantidadDeUsos = usos == 1 ? "1 uso" : usos + " usos";
        if (recuerdo instanceof RecuerdoDeHabilidad) {
            RecuerdoDeHabilidad habilidad = (RecuerdoDeHabilidad) recuerdo;
            int daño = habilidad.getDaño();
            if (usos == 1) {
                daño = daño / 2;
            }
            daño = daño + personaje.getBonusDaño();
            String ayuda = Dialogos.nombreElemento(habilidad.getElemento()) + " · " + daño + " de daño "
                    + (habilidad.isEnArea() ? "a todos los enemigos" : "a un enemigo") + " · " + cantidadDeUsos;
            if (usos == 1) {
                ayuda = ayuda + " (último uso: fragmentado, pega la mitad)";
            }
            return ayuda;
        } else if (recuerdo instanceof RecuerdoDeTrauma) {
            RecuerdoDeTrauma trauma = (RecuerdoDeTrauma) recuerdo;
            return Dialogos.nombreElemento(trauma.getElemento()) + " · " + (trauma.getDaño() + personaje.getBonusDaño())
                    + " de daño a un enemigo · te cuesta entre 15 y 25 de vida · uso único";
        } else if (recuerdo instanceof RecuerdoDePersona) {
            return "Resistencia del " + usos * 25 + "% a tu debilidad durante este combate · " + cantidadDeUsos;
        } else {
            int porcentaje = usos * 25;
            return "Cura el " + porcentaje + "% de tu vida máxima (" + personaje.getVidaMaxima() * porcentaje / 100
                    + " de vida) · " + cantidadDeUsos;
        }
    }

    private String textoUsos(Reutilizable recuerdo) {
        if (recuerdo.getUsosRestantes() == 1) {
            return "1 uso";
        }
        return recuerdo.getUsosRestantes() + " usos";
    }

    private String golpear(Enemigo enemigo, int daño, Elemento elemento) {
        int vidaAntes = enemigo.getVida();
        enemigo.recibirDaño(daño, elemento);
        int hecho = vidaAntes - enemigo.getVida();
        String texto = " Le hacés " + hecho + " de daño a la " + nombre(enemigo) + ".";
        if (elemento != null && elemento == enemigo.getDebilidad()) {
            texto = texto + " ¡Es débil al " + Dialogos.nombreElemento(elemento).toLowerCase() + "! (+50%)";
        }
        return texto + siCayo(enemigo);
    }

    private String turnoDeLosEnemigos(Personaje personaje) {
        String texto = "";
        for (Enemigo enemigo : enemigos) {
            if (enemigo.sigueEnCombate() && personaje.getVida() > 0) {
                Elemento elemento = enemigo.getElemento();
                int golpesAntes = personaje.getResistencias().getOrDefault(elemento, 0);
                int vidaAntes = personaje.getVida();
                enemigo.infligirDaño(personaje);
                int recibido = vidaAntes - personaje.getVida();
                String nombreElemento = Dialogos.nombreElemento(elemento).toLowerCase();
                texto = texto + "\n\nLa " + nombre(enemigo) + " te ataca con " + nombreElemento + ": te hace " + recibido + " de daño.";
                if (elemento == personaje.getDebilidad() && elemento == personaje.getResistenciaTemporal()) {
                    texto = texto + " Es tu debilidad (+50%), pero tu recuerdo te protege (-"
                            + personaje.getPorcentajeResistenciaTemporal() + "%).";
                } else if (elemento == personaje.getDebilidad()) {
                    texto = texto + " Es tu debilidad (+50%).";
                } else if (golpesAntes >= 3) {
                    texto = texto + " Lo resistís (mitad de daño).";
                } else if (golpesAntes == 2) {
                    texto = texto + " Ya aguantaste 3 golpes de " + nombreElemento + ": hasta el próximo bosque te hace la mitad.";
                }
            }
        }
        return texto;
    }

    private Enemigo elegirObjetivo(String texto) {
        int enPie = 0;
        for (Enemigo enemigo : enemigos) {
            if (enemigo.sigueEnCombate()) {
                enPie++;
            }
        }
        if (enPie == 1) {
            return primerEnemigoEnPie();
        }
        int posicion = Dialogos.elegirEnemigo(texto);
        return enemigos.get(posicion);
    }

    private void actualizarTarjetas() {
        String[] nombres = new String[enemigos.size()];
        String[] elementos = new String[enemigos.size()];
        int[] vidas = new int[enemigos.size()];
        int[] vidasMaximas = new int[enemigos.size()];
        for (int i = 0; i < enemigos.size(); i++) {
            Enemigo enemigo = enemigos.get(i);
            nombres[i] = nombre(enemigo);
            elementos[i] = Dialogos.nombreElemento(enemigo.getElemento());
            vidas[i] = enemigo.getVida();
            vidasMaximas[i] = enemigo.getVidaMaxima();
        }
        Dialogos.mostrarEnemigos(nombres, elementos, vidas, vidasMaximas);
    }

    private String presentacion() {
        if (enemigos.size() == 1) {
            return "De la niebla se desprende una figura oscura: la " + nombre(enemigos.get(0)) + ". Viene hacia vos.";
        }
        String texto = "De la niebla se desprenden " + enemigos.size() + " figuras oscuras: ";
        for (int i = 0; i < enemigos.size(); i++) {
            if (i > 0 && i == enemigos.size() - 1) {
                texto = texto + " y ";
            } else if (i > 0) {
                texto = texto + ", ";
            }
            texto = texto + "la " + nombre(enemigos.get(i));
        }
        return texto + ". Vienen hacia vos.";
    }

    private String siCayo(Enemigo enemigo) {
        if (enemigo.sigueEnCombate()) {
            return "";
        }
        return "\n\nLa " + nombre(enemigo) + " se deshace en la niebla.";
    }

    private boolean quedanEnemigos() {
        for (Enemigo enemigo : enemigos) {
            if (enemigo.sigueEnCombate()) {
                return true;
            }
        }
        return false;
    }

    private Enemigo primerEnemigoEnPie() {
        for (Enemigo enemigo : enemigos) {
            if (enemigo.sigueEnCombate()) {
                return enemigo;
            }
        }
        return null;
    }

    private String nombre(Enemigo enemigo) {
        String nombre = "Sombra de " + Dialogos.nombreElemento(enemigo.getElemento());
        int iguales = 0;
        int numero = 0;
        for (Enemigo otro : enemigos) {
            if (otro.getElemento() == enemigo.getElemento()) {
                iguales++;
                if (otro == enemigo) {
                    numero = iguales;
                }
            }
        }
        if (iguales > 1) {
            nombre = nombre + " " + numero;
        }
        return nombre;
    }
}