package core;

import enemigos.Enemigo;
import enemigos.LadronDeRecuerdos;
import excepciones.DisolucionException;
import excepciones.RecuerdoPerdidoException;
import enemigos.EnemigoBase;
import enemigos.GrupoDeEnemigos;
import enemigos.JefeFinal;
import enemigos.LadronDeRecuerdos   ;
import npcs.Mercader;
import npcs.GuardianDeRecuerdos;
import recuerdos.Recuerdo;
import recuerdos.RecuerdoDeHabilidad;
import recuerdos.RecuerdoDeLugar;
import recuerdos.RecuerdoDePersona;
import recuerdos.RecuerdoDeTrauma;
import recuerdos.Reutilizable;
import ui.Dialogos;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;

public class Juego {

    private Personaje jugador;
    private Escena escenaActual;
    private int puertasReveladas;
    private int encuentrosMercader;
    private Mercader mercader;
    private int ranura;

    public Juego() {
        this.jugador = new Personaje();
        this.escenaActual = null;
        this.puertasReveladas = 0;
        this.encuentrosMercader = 0;
        this.mercader = null;
        this.ranura = 1;
    }

    public void iniciar() {
        Dialogos.alGuardar(() -> guardarPartida());
        boolean otraRun = true;
        while (otraRun) {
            reiniciarRun();
            if (!continuarPartida()) {
                empezarRunNueva();
            }
            menuCastillo();
            otraRun = Dialogos.confirmar("La oscuridad vuelve a cerrarse. En algún lugar del Limbo, otro Verdugo está por abrir los ojos.\n\n"
                    + "No va a recordar nada de esta run. Solo lo que dejaste en el cofre del Guardián lo va a estar esperando.\n\n"
                    + "¿Empezás una run nueva?");
        }
        Dialogos.cerrar();
    }

    private boolean continuarPartida() {
        ArrayList<Integer> ocupadas = new ArrayList<>();
        for (int numero = 1; numero <= 3; numero++) {
            if (new File("partida_" + numero + ".txt").exists()) {
                ocupadas.add(numero);
            }
        }
        if (ocupadas.isEmpty()) {
            return false;
        }
        Dialogos.zona("El Limbo");
        Dialogos.actualizarEstado(jugador);
        int eleccion = Dialogos.elegir("Hay partidas guardadas. ¿Qué querés hacer?",
                new String[]{"Continuar partida", "Nueva run"});
        if (eleccion == 1) {
            return false;
        }
        String[] botones = new String[ocupadas.size() + 1];
        for (int i = 0; i < ocupadas.size(); i++) {
            botones[i] = "Ranura " + ocupadas.get(i) + " · " + resumenDeRanura(ocupadas.get(i));
        }
        botones[ocupadas.size()] = "← Volver";
        int elegida = Dialogos.elegir("¿Qué partida continuás?", botones);
        if (elegida == ocupadas.size()) {
            return continuarPartida();
        }
        ranura = ocupadas.get(elegida);
        if (!cargarPartida()) {
            Dialogos.mostrar("La partida de la ranura " + ranura + " está dañada y no se puede cargar. Empezás una run nueva.");
            return false;
        }
        Dialogos.zona("El Castillo");
        Dialogos.actualizarEstado(jugador);
        Dialogos.mostrar("Abrís los ojos en el patio del castillo. Todo está como lo dejaste.");
        return true;
    }

    private void empezarRunNueva() {
        elegirRanura();
        Dialogos.zona("El Limbo");
        Dialogos.actualizarEstado(jugador);
        Dialogos.mostrar("Abrís los ojos y no hay nada. Ni cielo, ni suelo, ni ruido.\n\n"
                + "Sabés quién sos: el Verdugo. En vida quitaste muchas vidas, y ninguna fue decisión tuya.\n\n"
                + "Y recordás algo más: recordás haber muerto.");
        Dialogos.mostrar("Esto es El Limbo. Si hay redención para alguien como vos, tiene que estar en algún lugar de esta oscuridad.\n\n"
                + "Caminás un poco, sin saber hacia dónde.");
        Dialogos.mostrar("Entonces las ves: burbujas flotando frente a vos que, en medio de toda la oscuridad, emiten luz.");
        elegirRecuerdosIniciales();
        llegarAlRefugio();
    }

    private void elegirRanura() {
        Dialogos.zona("El Limbo");
        Dialogos.actualizarEstado(jugador);
        boolean elegida = false;
        while (!elegida) {
            String[] botones = new String[3];
            for (int numero = 1; numero <= 3; numero++) {
                String resumen = resumenDeRanura(numero);
                botones[numero - 1] = "Ranura " + numero + " · " + (resumen == null ? "vacía" : resumen);
            }
            int opcion = Dialogos.elegir("¿En qué ranura se guarda esta run?", botones);
            ranura = opcion + 1;
            elegida = resumenDeRanura(ranura) == null
                    || Dialogos.confirmar("La ranura " + ranura + " ya tiene una partida. Si seguís, se va a sobrescribir. ¿Seguís?");
        }
    }

    private String resumenDeRanura(int numero) {
        int recuerdos = 0;
        String vida = "?";
        String monedas = "?";
        try (BufferedReader entrada = new BufferedReader(new FileReader("partida_" + numero + ".txt"))) {
            String linea = entrada.readLine();
            while (linea != null) {
                if (linea.startsWith("recuerdo=")) {
                    recuerdos++;
                } else if (linea.startsWith("vida=")) {
                    vida = linea.substring(5);
                } else if (linea.startsWith("monedas=")) {
                    monedas = linea.substring(8);
                }
                linea = entrada.readLine();
            }
        } catch (IOException e) {
            return null;
        }
        return recuerdos + " recuerdos · " + vida + " de vida · " + monedas + " monedas";
    }

    private void reiniciarRun() {
        jugador = new Personaje();
        escenaActual = null;
        puertasReveladas = 0;
        encuentrosMercader = 0;
        mercader = null;
    }

    public void otorgarRecompensa(Personaje personaje) {
        otorgarRecompensa(personaje, null);
    }

    public Escena generarBosque() {
        int escenas = 3 + (int) (Math.random() * 2);
        return new Escena(caminosHacia(escenas, new ArrayList<>()), null);
    }

    public void guardarPartida() {
        try (PrintWriter salida = new PrintWriter(new FileWriter("partida_" + ranura + ".txt"))) {
            salida.println("vida=" + jugador.getVida());
            salida.println("monedas=" + jugador.getMonedas());
            salida.println("debilidad=" + (jugador.getDebilidad() == null ? "-" : jugador.getDebilidad().name()));
            salida.println("llave=" + jugador.isTieneLlave());
            salida.println("recuerdoClave=" + jugador.isTieneRecuerdoClave());
            salida.println("puertasReveladas=" + puertasReveladas);
            salida.println("encuentroMercader=" + encuentrosMercader);
            for (Recuerdo recuerdo : jugador.getRecuerdos()) {
                salida.println(lineaDeRecuerdo(recuerdo));
            }
            Dialogos.avisar("Partida guardada en la ranura " + ranura + ".");
        } catch (IOException e ) {
            Dialogos.avisar("No se pudo guardar la partida.");
        }
    }

    private String lineaDeRecuerdo(Recuerdo recuerdo) {
        String tipo = "LUGAR";
        String elemento = "-";
        String enArea = "-";
        int daño = 0;
        int usos = 1;
        if (recuerdo instanceof RecuerdoDeHabilidad) {
            RecuerdoDeHabilidad habilidad = (RecuerdoDeHabilidad) recuerdo;
            tipo = "HABILIDAD";
            elemento = habilidad.getElemento().name();
            enArea = String.valueOf(habilidad.isEnArea());
            daño = habilidad.getDaño();
        } else if (recuerdo instanceof RecuerdoDeTrauma) {
            RecuerdoDeTrauma trauma = (RecuerdoDeTrauma) recuerdo;
            tipo = "TRAUMA";
            elemento = trauma.getElemento().name();
            daño = trauma.getDaño();
        } else if (recuerdo instanceof RecuerdoDePersona) {
            tipo = "PERSONA";
        }
        if (recuerdo instanceof Reutilizable) {
            usos = ((Reutilizable) recuerdo).getUsosRestantes();
        }
        return "recuerdo=" + tipo + "|" + recuerdo.getNombre() + "|" + recuerdo.getDescripcion() + "|"
                + elemento + "|" + enArea + "|" + daño + "|" + usos;
    }

    public boolean cargarPartida() {
        Personaje cargado = new Personaje();
        int vida = 0;
        int monedas = 0;
        Elemento debilidad = null;
        boolean llave = false;
        boolean recuerdoClave = false;
        int puertas = 0;
        int encuentros = 0;
        try (BufferedReader entrada = new BufferedReader(new FileReader("partida_" + ranura + ".txt"))) {
            String linea = entrada.readLine();
            while (linea != null) {
                String[] partes = linea.split("=", 2);
                if (partes.length == 2) {
                    String clave = partes[0];
                    String valor = partes[1];
                    if (clave.equals("vida")) {
                        vida = Integer.parseInt(valor);
                    } else if (clave.equals("monedas")) {
                        monedas = Integer.parseInt(valor);
                    } else if (clave.equals("debilidad") && !valor.equals("-")) {
                        debilidad = Elemento.valueOf(valor);
                    } else if (clave.equals("llave")) {
                        llave = Boolean.parseBoolean(valor);
                    } else if (clave.equals("recuerdoClave")) {
                        recuerdoClave = Boolean.parseBoolean(valor);
                    } else if (clave.equals("puertasReveladas")) {
                        puertas = Integer.parseInt(valor);
                    } else if (clave.equals("encuentrosMercader")) {
                        encuentros = Integer.parseInt(valor);
                    } else if (clave.equals("recuerdo")) {
                        Recuerdo recuerdo = recuerdoDeLinea(valor);
                        if (recuerdo != null) {
                            cargado.agregarRecuerdo(recuerdo);
                        }
                    }
                }
                linea = entrada.readLine();
            }
        } catch (IOException | IllegalArgumentException e) {
            return false;
        }
        if (cargado.getRecuerdos().isEmpty()) {
            return false;
        }
        cargado.setDebilidad(debilidad);
        cargado.setVida(Math.min(vida, cargado.getVidaMaxima()));
        cargado.setMonedas(monedas);
        cargado.setTieneLlave(llave);
        cargado.setTieneRecuerdoClave(recuerdoClave);
        jugador = cargado;
        puertasReveladas = puertas;
        encuentrosMercader = encuentros;
        if (encuentrosMercader >= 3) {
            mercader = new Mercader(stockDelMercader());
        }
        return true;
    }

    private Recuerdo recuerdoDeLinea(String datos) {
        String[] partes = datos.split("\\|");
        if (partes.length != 7) {
            return null;
        }
        String nombre = partes[1];
        String descripcion = partes[2];
        int daño = Integer.parseInt(partes[5]);
        int usos = Integer.parseInt(partes[6]);
        if (partes[0].equals("HABILIDAD")) {
            RecuerdoDeHabilidad habilidad = new RecuerdoDeHabilidad(nombre, descripcion,
                    Elemento.valueOf(partes[3]), Boolean.parseBoolean(partes[4]));
            habilidad.setDaño(daño);
            habilidad.setUsosRestantes(usos);
            return habilidad;
        } else if (partes[0].equals("TRAUMA")) {
            RecuerdoDeTrauma trauma = new RecuerdoDeTrauma(nombre, descripcion, Elemento.valueOf(partes[3]));
            trauma.setDaño(daño);
            return trauma;
        } else if (partes[0].equals("PERSONA")) {
            RecuerdoDePersona persona = new RecuerdoDePersona(nombre, descripcion);
            persona.setUsosRestantes(usos);
            return persona;
        } else if (partes[0].equals("LUGAR")) {
            RecuerdoDeLugar lugar = new RecuerdoDeLugar(nombre, descripcion);
            lugar.setUsosRestantes(usos);
            return lugar;
        }
        return null;
    }

    private void llegarAlRefugio() {
        Dialogos.zona("El Castillo");
        Dialogos.mostrar("Seguís caminando por el camino que se abrió frente a vos. Después de un rato, algo se recorta en la oscuridad: "
                + "una estructura enorme, muy difusa, como si la vieras a través de la niebla.\n\n"
                + "No sabés qué es, pero entendés que es un lugar para, por lo menos, refugiarte.");
        Dialogos.mostrar("Desde la entrada mirás hacia atrás. A tu alrededor, entre la niebla, hay un bosque.\n\n"
                + "Adentro del refugio no se ve nada. Lo único que podés hacer es salir al bosque.");
    }

    private void menuCastillo() {
        boolean enElJuego = true;
        while (enElJuego) {
            Dialogos.zona("El Castillo");
            Dialogos.actualizarEstado(jugador);
            Dialogos.permitirGuardar(true);
            revelarPuertas();
            guardarPartida();

            ArrayList<String> opciones = new ArrayList<>();
            opciones.add("Salir al bosque");
            if (encuentrosMercader >= 3) {
                opciones.add("Comerciar con el Mercader");
            }
            if (puertasReveladas >= 1 && !jugador.isTieneLlave()) {
                opciones.add("Acercarte a la puerta del ala este");
            }
            if (puertasReveladas >= 2) {
                opciones.add("Acercarte a la puerta enorme");
            }
            if (puertasReveladas >= 3 && !jugador.isTieneRecuerdoClave()) {
                opciones.add("Acercarte a la puerta del ala oeste");
            }

            int elegida = Dialogos.elegir(descripcionCastillo(), opciones.toArray(new String[0]));
            Dialogos.permitirGuardar(false);
            String opcion = opciones.get(elegida);

            try {
                if (opcion.equals("Salir al bosque")) {
                    recorrerBosque();
                } else if (opcion.equals("Comerciar con el Mercader")) {
                    Dialogos.zona("El Castillo");
                    Dialogos.mostrar("En un rincón del patio, el Mercader acomodó su manta como si siempre hubiera vivido ahí."
                            + "Te ve llegar y sonríe sin dientes.");
                    mercader.interactuar(jugador);
                } else if (opcion.equals("Acercarte a la puerta del ala este")) {
                    puertaEste();
                } else if (opcion.equals("Acercarte a la puerta del ala oeste")) {
                    puertaOeste();
                } else if (opcion.equals("Acercarte a la puerta enorme")) {
                    if (puertaJefe()) {
                        enElJuego = false;
                    }
                }
            } catch (DisolucionException e) {
                Dialogos.zona("La oscuridad");
                Dialogos.actualizarEstado(jugador);
                Dialogos.mostrar(e.getMessage());
            } catch (RecuerdoPerdidoException e) {
                muerteDefinitiva(e.getMessage());
                enElJuego = false;
            }
        }
    }

    private void puertaEste() {
        if (tieneRecuerdo("La casa de la infancia")) {
            Dialogos.mostrar("Apoyás la mano en la madera y te acordás de un patio con baldosas rotas y una pureta que refleja bienvenida. "
                    + "Esta se abre sola.\n\n"
                    + "Adentro hay una mesa, y sobre ella, una llave de hierro.");
            jugador.setTieneLlave(true);
            Dialogos.actualizarEstado(jugador);
            Dialogos.mostrar("Tomás la llave del castillo. Cuando te das vuelta, la puerta ya no está.");
        } else {
            Dialogos.mostrar("Te acercás a la puerta de madera gastada. Está trabada: no se abre.\n\n"
                    + "Sentís que se abre con un recuerdo. Para entrar necesitás el lugar \"La casa de la infancia\".");
        }
    }

    private void puertaOeste() {
        if (tieneRecuerdo("La voz de mamá")) {
            Dialogos.mostrar("La voz vuelve a llamarte para entrar. Esta vez la reconocés.\n\n"
                    + "La puerta se abre.");
            jugador.setTieneRecuerdoClave(true);
            Dialogos.actualizarEstado(jugador);
            Dialogos.mostrar("Adentro te espera un último recuerdo: El Último Grito. La última ejecución antes de tu muerte. "
                    + "El condenado gritó una sola palabra, y era tu nombre.\n\n"
                    + "Cuando salís, la puerta ya no está.");
        } else {
            Dialogos.mostrar("Te acercás a la puerta de madera del ala oeste, idéntica a la del ala este. "
                    + "Del otro lado se oye una voz, perdida en el fondo de tu alma.\n\n"
                    + "Sentís que se abre con un recuerdo. Para entrar necesitás la Persona \"La voz de mamá\".");
        }
    }

    private boolean puertaJefe() throws DisolucionException, RecuerdoPerdidoException {
        if (jugador.isTieneLlave() && jugador.isTieneRecuerdoClave()) {
            boolean entrar = Dialogos.confirmar("La oscuridad se retira por completo. La puerta es enorme, mitad de hierro negro, "
                    + "mitad oscuridad absoluta. Del otro lado alguien te espera.\n\n"
                    + "Tenés la llave y el recuerdo clave. ¿Entrás?");
            if (entrar) {
                return combateFinal();
            }
        } else {
            String faltan = "";
            if (!jugador.isTieneLlave()) {
                faltan = faltan + "\n- la llave del castillo";
            }
            if (!jugador.isTieneRecuerdoClave()) {
                faltan = faltan + "\n- un recuerdo que todavía no tenés";
            }
            Dialogos.mostrar("Te acercas a la puerta enorme. La oscuridad no te deja verla entera: solo distinguís una cerradura "
                    + "y, al lado, un hueco donde parece que va un burbuja de recuerdo.\n\nPara abrirla te falta:" + faltan);
        }
        return false;
    }

    private boolean combateFinal() throws DisolucionException, RecuerdoPerdidoException {
        jugador.reiniciarResistencias();
        Dialogos.zona("La sala del Juez");
        Dialogos.mostrar("Metés la llave en la cerradura y apoyás el último grito en el hueco de al lado. La puerta se abre sin ruido.\n\n"
                + "Del otro lado hay una sala enorme, vacía, con el piso cubierto de ceniza. Huele a plaza después de una ejecución.");
        Elemento elemento = elementoAlAzar();
        JefeFinal juez = new JefeFinal(elemento, 90, debilidadDe(elemento), 15, 2);
        ArrayList<Enemigo> enemigos = new ArrayList<>();
        enemigos.add(juez);
        boolean ganaste = new GrupoDeEnemigos(enemigos).interactuar(jugador);
        if (ganaste) {
            victoria();
        } else {
            jugador.disolver();
        }
        return ganaste;
    }

    private void victoria() {
        Dialogos.zona("La redención");
        Dialogos.actualizarEstado(jugador);
        Dialogos.mostrar("El martillo del Juez rueda por el piso y se deshace en ceniza. Detrás del estrado se abre una grieta de luz.\n\n"
                + "Por primera vez en toda tu vida, y en toda tu muerte, nadie te dice qué hacer.");
        Dialogos.mostrar("Cruzás la luz con tus recuerdos encima: los que te dolían y los que te salvaron.\n\n"
                + "El Verdugo encontró su redención.\n\n");
    }

    private void recorrerBosque() throws DisolucionException, RecuerdoPerdidoException {
        jugador.reiniciarResistencias();
        mercader = new Mercader(stockDelMercader());
        Dialogos.zona("El bosque");
        Dialogos.mostrar("Salís del castillo y la niebla se cierra detrás de tus pasos. "
                + "No hay vuelta atrás hasta llegar al final del bosque.");
        Escena bosque = generarBosque();
        boolean sobreviviste = bosque.resolver(jugador);
        if (sobreviviste) {
            Dialogos.zona("El Castillo");
            Dialogos.mostrar("Entre los árboles vuelve a recortarse el castillo. Volvés a refugiarte.");
        } else {
            jugador.disolver();
        }
    }

    private ArrayList<Opcion> caminosHacia(int restantes, ArrayList<String> usados) {
        ArrayList<Opcion> opciones = new ArrayList<>();
        if (restantes == 0) {
            opciones.add(new Opcion("Volver al castillo"));
            return opciones;
        }
        int cantidad = 1 + (int) (Math.random() * 3);
        for (int i = 0; i < cantidad; i++) {
            ArrayList<String> usadosEnEsteCamino = new ArrayList<>(usados);
            Interactuable encuentro = sortearEncuentro(usadosEnEsteCamino);
            Escena escena = new Escena(caminosHacia(restantes - 1, usadosEnEsteCamino), encuentro);
            opciones.add(new Opcion(nombreDeCamino(i, cantidad), null, escena));
        }
        return opciones;
    }

    private String nombreDeCamino(int numero, int cantidad) {
        if (cantidad == 1) {
            return "Seguir el sendero";
        }
        if (cantidad == 2) {
            String[] nombres = {"Camino de la izquierda", "Camino de la derecha"};
            return nombres[numero];
        }
        String[] nombres = {"Camino de la izquierda", "Camino del medio", "Camino de la derecha"};
        return nombres[numero];
    }

    private Interactuable sortearEncuentro(ArrayList<String> usados) {
        double azar = Math.random();
        String tipo;
        if (azar < 0.50) {
            tipo = "combate";
        } else if (azar < 0.65) {
            tipo = "ladron";
        } else if (azar < 0.80) {
            tipo = "mercader";
        } else if (azar < 0.90) {
            tipo = "guardian";
        } else {
            tipo = "claro";
        }
        if (!tipo.equals("combate") && usados.contains(tipo)) {
            tipo = "combate";
        }
        usados.add(tipo);

        switch (tipo) {
            case "ladrón":
                return personaje -> encuentroConLadron();
            case "mercader":
                return personaje -> encuentroConMercader();
            case "guardian":
                return new GuardianDeRecuerdos();
            case "claro":
                return personaje -> claroEnCalma();
            default:
                return personaje -> combateDelBosque();
        }
    }



    private boolean claroEnCalma() {
        Dialogos.zona("Un claro en calma");
        int vidaAntes = jugador.getVida();
        int cura = jugador.getVidaMaxima() * 15 / 100;
        jugador.setVida(Math.min(jugador.getVidaMaxima(), jugador.getVida() + cura));
        Dialogos.actualizarEstado(jugador);
        Dialogos.mostrar("Los árboles se abren en un claro. No hay niebla, no hay sombras: solo silencio y un poco de luz.\n\n"
                + "Te sentás un rato. Recuperás " + (jugador.getVida() - vidaAntes) + " de vida.");
        return true;
    }

    private boolean combateDelBosque() {
        boolean masFuertes = jugador.getRecuerdos().size() >= 12 && Math.random() < 0.5;
        ArrayList<Enemigo> sombras = new ArrayList<>();
        int cantidad = 1 + (int) (Math.random() * 3);
        for (int i = 0; i < cantidad; i++) {
            sombras.add(crearSombra(masFuertes));
        }
        if (masFuertes) {
            Dialogos.zona("El bosque");
            Dialogos.mostrar("La niebla se vuelve más espesa. Las sombras de este lado del bosque son más densas, más grandes.");
        }
        GrupoDeEnemigos combate = new GrupoDeEnemigos(sombras);
        boolean ganaste = combate.interactuar(jugador);
        if (ganaste) {
            darMonedas(sombras.size());
            Elemento tendencia = sombras.get((int) (Math.random() * sombras.size())).getElemento();
            otorgarRecompensa(jugador, tendencia);
        }
        return ganaste;
    }

    private boolean encuentroConLadron() {
        Elemento elemento = elementoAlAzar();
        int vida = 30 + (int) (Math.random() * 11);
        LadronDeRecuerdos ladron = new LadronDeRecuerdos(elemento, vida, debilidadDe(elemento), 0);
        ArrayList<Enemigo> enemigos = new ArrayList<>();
        enemigos.add(ladron);
        boolean sobreviviste = new GrupoDeEnemigos(enemigos).interactuar(jugador);
        if (!sobreviviste) {
            return false;
        }

        boolean vencido = ladron.getVida() <= 0;
        int monedas;
        String texto;
        if (vencido) {
            monedas = 12 + (int) (Math.random() * 9);
            texto = "El Ladrón de Recuerdos no se levanta. Entre sus harapos encuentras " + monedas + " monedas.";
        } else {
            monedas = 5 + (int) (Math.random() * 6);
            texto = "En la huida se le cae una bolsita del cinturon: " + monedas + " monedas.";
        }
        ArrayList<Recuerdo> botin = ladron.getBotin();
        if (!botin.isEmpty()) {
            StringBuilder robados = new StringBuilder();
            for (int i = 0; i < botin.size(); i++) {
                if (i > 0) {
                    robados.append(", ");
                }
                robados.append(botin.get(i).getNombre());
            }
            texto = texto + "\n\nLo que te robó no vuelve " + robados + ".";
        }
        jugador.setMonedas(jugador.getMonedas() + monedas);
        Dialogos.zona("Después del combate");
        Dialogos.actualizarEstado(jugador);
        Dialogos.mostrar(texto);
        if(vencido) {
            otorgarRecompensa(jugador, ladron.getElemento());
            }
        return true;
    }

    private boolean encuentroConMercader() {
        int monedasAntes = jugador.getMonedas();
        mercader.interactuar((jugador));
        if (encuentrosMercader < 3 && jugador.getMonedas() < monedasAntes) {
            encuentrosMercader++;
            if (encuentrosMercader == 3) {
                Dialogos.mostrar("Antes que te vayas, el Mercader te mira un rato largo.\n\n"
                        + "\"Ya sos cliente de la casa. Este bosque se está poniendo cada vez mas feo para un viejo como yo..."
                        + "Si no te molesta, me voy a instalar en tu castillo, te voy a ser de ayuda, vas a poder comprarme ahí también."
                        + "Además voy a seguir saliendo a buscar mercancia, asi que no te preocupes por extrañarme en tus salidas.\"");
            }
        }
        return true;
    }

    private void muerteDefinitiva(String mensaje) {
        new File("partida_" + ranura + ".txt").delete();
        Dialogos.zona("El final");
        Dialogos.actualizarEstado(jugador);
        Dialogos.mostrar(mensaje);
        Dialogos.mostrar("El Verdugo se apaga en la oscuridad del Limbo, sin nada que recordar.\n\n");
    }

    private void darMonedas(int cantidadEnemigos) {
        int monedas;
        if (cantidadEnemigos == 1) {
            monedas = 10 + (int) (Math.random() * 6);
        } else if (cantidadEnemigos == 2) {
            monedas = 20 + (int) (Math.random() * 6);
        } else {
            monedas = 30 + (int) (Math.random() * 11);
        }
        jugador.setMonedas(jugador.getMonedas() + monedas);
        Dialogos.zona("Después del combate");
        Dialogos.actualizarEstado(jugador);
        Dialogos.mostrar("Las Sombras se deshacen en humo. Entre la ceniza quedan " + monedas + " monedas.");
    }

    private void otorgarRecompensa(Personaje personaje, Elemento tendencia) {
        Recuerdo nuevo = recuerdoNuevo(tendencia);
        ArrayList<Recuerdo> gastados = recuerdosGastados(personaje);

        ArrayList<String> opciones = new ArrayList<>();
        String texto = "Entre el humo flota una burbuja de luz.";
        if (nuevo != null) {
            opciones.add("Quedarte con el recuerdo");
            texto = texto + " Adentro se mueve un recuerdo:\n\n" + nombreConDetalle(nuevo) + ": " + nuevo.getDescripcion();
        }
        if (!gastados.isEmpty()) {
            opciones.add("Usar la luz para recargar un recuerdo");
        }
        if (opciones.isEmpty()) {
            Dialogos.mostrar("Entre el humo no queda nada más: no hay recuerdos nuevos ni nada que recargar.");
            return;
        }

        String[] ayudas = new String[opciones.size()];
        for (int i = 0; i < opciones.size(); i++) {
            if (opciones.get(i).equals("Quedarte con el recuerdo")) {
                ayudas[i] = ayudaDeRecuerdo(nuevo);
            }
        }
        int elegida = Dialogos.elegir(texto, opciones.toArray(new String[0]), ayudas);
        if (opciones.get(elegida).equals("Quedarte con el recuerdo")) {
            int bonusAntes = personaje.getBonusDaño();
            personaje.agregarRecuerdo(nuevo);
            Dialogos.actualizarEstado(personaje);
            String mensaje = "La burbuja se deshace en tus manos. Ahora " + nuevo.getNombre() + " es tuyo.";
            if (personaje.getBonusDaño() > bonusAntes) {
                mensaje = mensaje + "\n\nCon cada recuerdo que juntás te sentís más entero: "
                        + "+5 de vida máxima y +2 de daño.";
            }
            Dialogos.mostrar(mensaje);
        } else {
            String[] botones = new String[gastados.size()];
            String[] ayudasRecarga = new String[gastados.size()];
            for (int i = 0; i < gastados.size(); i++) {
                Recuerdo recuerdo = gastados.get(i);
                botones[i] = recuerdo.getNombre() + " · " + ((Reutilizable) recuerdo).getUsosRestantes() + "/3 usos";
                ayudasRecarga[i] = "Recargado: " + Dialogos.textoAyuda(personaje, recuerdo, 3);
            }
            int elegido = Dialogos.elegir("¿Qué recuerdo querés recargar?", botones, ayudasRecarga);
            Recuerdo recargado = gastados.get(elegido);
            ((Reutilizable) recargado).setUsosRestantes(3);
            Dialogos.actualizarEstado(personaje);
            Dialogos.mostrar("La luz entra en " + recargado.getNombre() + " y el recuerdo vuelve a estar entero: 3/3 usos.");
        }
    }

    private Recuerdo recuerdoNuevo(Elemento tendencia) {
        ArrayList<Recuerdo> posibles = new ArrayList<>();
        for (Recuerdo recuerdo : todosLosRecuerdos()) {
            if (!tieneRecuerdo(recuerdo.getNombre())) {
                posibles.add(recuerdo);
            }
        }
        if (posibles.isEmpty()) {
            return null;
        }
        if (tendencia != null && Math.random() < 0.5) {
            ArrayList<Recuerdo> delElemento = new ArrayList<>();
            for (Recuerdo recuerdo : posibles) {
                if (elementoDe(recuerdo) == tendencia) {
                    delElemento.add(recuerdo);
                }
            }
            if (!delElemento.isEmpty()) {
                posibles = delElemento;
            }
        }
        return posibles.get((int) (Math.random() * posibles.size()));
    }

    private Recuerdo[] stockDelMercader() {
        ArrayList<Recuerdo> posibles = new ArrayList<>();
        for (Recuerdo recuerdo : todosLosRecuerdos()) {
            if (!tieneRecuerdo(recuerdo.getNombre())) {
                posibles.add(recuerdo);
            }
        }
        Collections.shuffle(posibles);
        Recuerdo[] stock = new Recuerdo[3];
        for (int i = 0; i < stock.length && i < posibles.size(); i++) {
            stock[i] = posibles.get(i);
        }
        return stock;
    }

    private ArrayList<Recuerdo> todosLosRecuerdos() {
        ArrayList<Recuerdo> todos = new ArrayList<>();
        todos.addAll(crearHabilidades());
        todos.addAll(crearPersonas());
        todos.addAll(crearLugares());
        todos.addAll(crearTraumas());
        todos.add(new RecuerdoDePersona("La voz de mamá", "Te llamaba para entrar a casa. Nunca supiste si estaba enojada o asustada."));
        todos.add(new RecuerdoDeLugar("La casa de la infancia", "Un patio con baldosas rotas y una puerta que nunca cerraba bien."));
        return todos;
    }

    private ArrayList<Recuerdo> recuerdosGastados(Personaje personaje) {
        ArrayList<Recuerdo> gastados = new ArrayList<>();
        for (Recuerdo recuerdo : personaje.getRecuerdos()) {
            if (recuerdo instanceof Reutilizable && ((Reutilizable) recuerdo).getUsosRestantes() < 3) {
                gastados.add(recuerdo);
            }
        }
        return gastados;
    }

    private Elemento elementoDe(Recuerdo recuerdo) {
        if (recuerdo instanceof RecuerdoDeHabilidad) {
            return ((RecuerdoDeHabilidad) recuerdo).getElemento();
        }
        if (recuerdo instanceof RecuerdoDeTrauma) {
            return ((RecuerdoDeTrauma) recuerdo).getElemento();
        }
        return null;
    }

    private String nombreConDetalle(Recuerdo recuerdo) {
        if (recuerdo instanceof RecuerdoDeHabilidad) {
            RecuerdoDeHabilidad habilidad = (RecuerdoDeHabilidad) recuerdo;
            String alcance = habilidad.isEnArea() ? ", a todos" : ", a uno";
            return "⚔ " + habilidad.getNombre() + " (" + Dialogos.nombreElemento(habilidad.getElemento()) + alcance + ")";
        }
        if (recuerdo instanceof RecuerdoDeTrauma) {
            return "✶ " + recuerdo.getNombre() + " (" + Dialogos.nombreElemento(((RecuerdoDeTrauma) recuerdo).getElemento()) + ", uso único)";
        }
        if (recuerdo instanceof RecuerdoDePersona) {
            return "♥ " + recuerdo.getNombre() + " (Persona: protege)";
        }
        return "⌂ " + recuerdo.getNombre() + " (Lugar: cura)";
    }

    private Enemigo crearSombra(boolean masFuerte) {
        Elemento elemento = elementoAlAzar();
        int vida = 25 + (int) (Math.random() * 16);
        int daño = 8 + (int) (Math.random() * 5);
        if (masFuerte) {
            vida = vida * 115 / 100;
            daño = daño + 2 + (int) (Math.random() * 2);
        }
        return new EnemigoBase(elemento, vida, debilidadDe(elemento), daño);
    }

    private Elemento debilidadDe(Elemento elemento) {
        switch (elemento) {
            case FUEGO:
                return Elemento.AGUA;
            case AGUA:
                return Elemento.RAYO;
            case RAYO:
                return Elemento.VENENO;
            default:
                return Elemento.FUEGO;
        }
    }

    private Elemento elementoAlAzar() {
        Elemento[] elementos = Elemento.values();
        return elementos[(int) (Math.random() * elementos.length)];
    }

    private boolean tieneRecuerdo(String nombre) {
        for (Recuerdo recuerdo : jugador.getRecuerdos()) {
            if (recuerdo.getNombre().equals(nombre)) {
                return true;
            }
        }
        return false;
    }

    private void revelarPuertas() {
        int cantidad = jugador.getRecuerdos().size();
        if (puertasReveladas < 1 && cantidad >= 6) {
            puertasReveladas = 1;
            Dialogos.mostrar("Al volver al castillo notas que el entorno se ve mas nitido, las sombras se van esclareciendo, el castillo tomando su forma original. "
                    + "Las paredes asperas de piedras en forma de ladrillos, el suelo de un granito liso, resquebrajado y agrietado por los años.\n\n"
                    + "Pero no es todo, el castillo todavia es difuso en algunas zonas, fragmentos de sombra, una densa oscuridad infinita como la primera vez que despertaste, "
                    + "pareciera que intenta esconder algo.\n\n"
                    + "En el ala este se descubre una puerta de madera gastada, tanto o mas que el mismo castillo.");
        }
        if (puertasReveladas < 2 && cantidad >= 8) {
            puertasReveladas = 2;
            Dialogos.mostrar("Otra vez el castillo cambió, ahora ves un techo aunque caido a pedazos, trozos de madera astillados balanceandose por hilos de fibras rotas. "
                    + "Las arañas sin velas oxidadas, las antorchas en las paredes reducidas a cenizas.\n\n"
                    + "Al fondo, donde la oscuridad cesa a pesar de su amargura, se entrevé una puerta enorme. "
                    + "No llegas a verla en detalle, pero lo que espera al otro lado es evidentemente abrumador, desprende un aura de panico indescriptible para el verdugo. Como si en cierto punto supiese lo que le espera.");
        }
        if (puertasReveladas < 3 && cantidad >= 10) {
            puertasReveladas = 3;
            Dialogos.mostrar("El castillo y todo su alrededor ya cobra el color y la vida (o lo que queda de ella) que deberia. Ya podes verlo casi como cuando lo veias con tus propios ojos en vida.\n\n"
                    + "En el ala oeste se revela una puerta de madera, identica a la del ala este, de ella se oye una voz perdida en el fondo de tu alma.");
        }
    }

    private String textoDelMercader() {
        if (encuentrosMercader >= 3) {
            return "\n\nEn un rincón del patio, el Mercader tiene su manta extendida.";
        }
        return "";
    }

    private String descripcionCastillo() {
        if (puertasReveladas == 0) {
            return "El castillo esta envuelto en niebla. Apenas se distinguen muros y diagonales. "
                    + "Adentro no se ve nada." + textoDelMercader() + "\n\nAfuera, el bosque espera.";
        }
        StringBuilder texto = new StringBuilder("La oscuridad cesa, se ven muros de piedra, un patio y corredores vacíos.");
        if (!jugador.isTieneLlave()) {
            texto.append("\n\nEn el ala este hay una puerta de madera maltratada por el tiempo.");
        }
        if (puertasReveladas >= 2) {
            if (jugador.isTieneLlave() && jugador.isTieneRecuerdoClave()) {
                texto.append("\n\nAl fondo, la oscuridad ya no tapa nada: la puerta del jefe está a la vista, enorme, de hierro negro.");
            } else {
                texto.append("\n\nAl fondo, en la oscuridad, se entrevé una puerta enorme.");
            }
        }
        if (puertasReveladas >= 3 && !jugador.isTieneRecuerdoClave()) {
            texto.append("\n\nEn el ala oeste hay una puerta idéntica a la del ala este, se oye una voz.");
        }
        texto.append(textoDelMercader());
        texto.append("\n\nAfuera, el bosque espera.");
        return texto.toString();
    }

    private void elegirRecuerdosIniciales() {
        Dialogos.zona("Tus recuerdos");
        Dialogos.mostrar("Te acercás a las burbujas. No son luces: son recuerdos. Tus recuerdos.\n\n"
                + "Hay de cuatro tipos, tres de cada uno. De cada tipo vas a poder quedarte con uno solo.");

        elegirUnoDe(crearHabilidades(), "Tres burbujas se acercan. Adentro de cada una se mueve algo que sabías hacer:");
        Dialogos.mostrar("La burbuja se deshace en tus manos. Alrededor, la oscuridad ya no es tan espesa.");

        elegirUnoDe(crearPersonas(), "Otras tres burbujas. Adentro hay caras que conocías:");
        Dialogos.mostrar("La oscuridad retrocede un poco más. Ahora distinguís el suelo bajo tus pies.");

        elegirUnoDe(crearLugares(), "Tres burbujas más. Adentro hay lugares donde estuviste:");
        Dialogos.mostrar("Ya no es una oscuridad infinita: frente a vos se dibuja, por lo menos, un camino.");

        elegirUnoDe(crearTraumas(), "Quedan tres burbujas, más oscuras que las demás. Sabés que tenés que agarrar una:");
        Dialogos.mostrar("Este recuerdo duele. Desde ahora, el " + Dialogos.nombreElemento(jugador.getDebilidad()).toLowerCase()
                + " te hace más daño: es tu debilidad.");
    }

    private String ayudaDeRecuerdo(Recuerdo recuerdo) {
        StringBuilder ayuda = new StringBuilder("<html>");
        StringBuilder linea = new StringBuilder();
        for (String palabra : recuerdo.getDescripcion().split(" ")) {
            if (linea.length() > 0 && linea.length() + palabra.length() >= 60) {
                ayuda.append(linea).append("<br>");
                linea = new StringBuilder();
            }
            if (linea.length() > 0) {
                linea.append(" ");
            }
            linea.append(palabra);
        }
        return ayuda.append(linea).append("</html>").toString();
    }

    private void elegirUnoDe(ArrayList<Recuerdo> opciones, String texto) {
        Collections.shuffle(opciones);
        StringBuilder narracion = new StringBuilder(texto);
        String[] botones = new String[3];
        String[] ayudas = new String[3];
        for (int i = 0; i < 3; i++) {
            Recuerdo recuerdo = opciones.get(i);
            String nombre = recuerdo.getNombre();
            if (recuerdo instanceof RecuerdoDeHabilidad) {
                RecuerdoDeHabilidad habilidad = (RecuerdoDeHabilidad) recuerdo;
                String alcance = habilidad.isEnArea() ? ", a todos" : ", a uno";
                nombre = nombre + " (" + Dialogos.nombreElemento(habilidad.getElemento()) + alcance + ")";
            } else if (recuerdo instanceof RecuerdoDeTrauma) {
                nombre = nombre + " (" + Dialogos.nombreElemento(((RecuerdoDeTrauma) recuerdo).getElemento()) + ")";
            }
            botones[i] = nombre;
            ayudas[i] = ayudaDeRecuerdo(recuerdo);
            narracion.append("\n\n").append(nombre).append(": ").append(recuerdo.getDescripcion());
        }
        int elegido = Dialogos.elegir(narracion.toString(), botones, ayudas);
        jugador.agregarRecuerdo(opciones.get(elegido));
        Dialogos.actualizarEstado(jugador);
    }

    private ArrayList<Recuerdo> crearHabilidades() {
        ArrayList<Recuerdo> habilidades = new ArrayList<>();
        habilidades.add(new RecuerdoDeHabilidad("Hachazo ardiente", "El filo del hacha se prende fuego y corta quemando la carne.", Elemento.FUEGO, false));
        habilidades.add(new RecuerdoDeHabilidad("Lluvia de brasas", "Arrojás un puñado de brasas encendidas que se pegan a la piel de todos los enemigos.", Elemento.FUEGO, true));
        habilidades.add(new RecuerdoDeHabilidad("Ahogamiento", "Una mano de agua helada le aprieta la garganta al enemigo hasta dejarlo sin aire.", Elemento.AGUA, false));
        habilidades.add(new RecuerdoDeHabilidad("Látigo de agua", "Un chorro de agua barre el campo como un látigo y abre la piel de todos los que alcanza.", Elemento.AGUA, true));
        habilidades.add(new RecuerdoDeHabilidad("Golpe de tormenta", "Levantás el hacha al cielo y la tormenta descarga sus rayos sobre todos los enemigos.", Elemento.RAYO, true));
        habilidades.add(new RecuerdoDeHabilidad("Relámpago", "Un relámpago corto salta de tu mano y atraviesa al enemigo de lado a lado.", Elemento.RAYO, false));
        habilidades.add(new RecuerdoDeHabilidad("Filo envenenado", "La hoja del hacha está untada en veneno, y cada corte sigue quemando por dentro.", Elemento.VENENO, false));
        habilidades.add(new RecuerdoDeHabilidad("Niebla tóxica", "Una nube verdosa envuelve a los enemigos y les quema los pulmones al respirar.", Elemento.VENENO, true));
        habilidades.add(new RecuerdoDeHabilidad("Nido de víboras", "Soltás un saco de víboras a los pies del enemigo. Lo rodean y lo muerden hasta que cae.", Elemento.VENENO, false));
        habilidades.add(new RecuerdoDeHabilidad("Cadena de tormenta", "Lanzás una cadena de hierro contra el enemigo y un rayo la recorre de punta a punta, sacudiéndolo hasta los huesos.", Elemento.RAYO, false));
        habilidades.add(new RecuerdoDeHabilidad("Aguja de agua", "Concentrás el agua en un hilo finísimo y la disparás con tanta fuerza que atraviesa la armadura y la carne como una aguja.", Elemento.AGUA, false));
        habilidades.add(new RecuerdoDeHabilidad("Brea ardiente", "Arrojás brea encendida que se le pega al cuerpo y no se apaga por más que se sacuda.", Elemento.FUEGO, false));
        habilidades.add(new RecuerdoDeHabilidad("Muro de llamas", "Levantás una pared de fuego que avanza y quema a todos los que encuentra a su paso.", Elemento.FUEGO, true));
        habilidades.add(new RecuerdoDeHabilidad("Marea negra", "Una ola oscura inunda el campo y arrastra a todos los enemigos a la vez.", Elemento.AGUA, true));
        habilidades.add(new RecuerdoDeHabilidad("Tormenta de hierro", "Clavás cadenas en el suelo y el rayo salta de una a otra, golpeando a todos los que tocan.", Elemento.RAYO, true));
        habilidades.add(new RecuerdoDeHabilidad("Humo de hierbas", "Quemás un puñado de hierbas venenosas y el humo se mete en los pulmones de todos.", Elemento.VENENO, true));
        return habilidades;
    }

    private ArrayList<Recuerdo> crearPersonas() {
        ArrayList<Recuerdo> personas = new ArrayList<>();
        personas.add(new RecuerdoDePersona("El abrazo de la abuela", "Olía a pan y a jabón, y duraba un poco más de lo necesario."));
        personas.add(new RecuerdoDePersona("El amigo del barrio", "Siempre te esperaba en la esquina, aunque llegaras tarde."));
        personas.add(new RecuerdoDePersona("La tutora", "Te enseñó a leer despacio, sin apurarte nunca."));
        personas.add(new RecuerdoDePersona("El hermano menor", "Te seguía a todos lados, y vos fingías que te molestaba."));
        personas.add(new RecuerdoDePersona("El maestro", "Te enseñó a sostener el hacha sin que te temblara el pulso y a no dejar nunca una mancha. Su voz áspera fue lo más parecido a un padre que tuviste."));
        personas.add(new RecuerdoDePersona("El sacerdote", "Te escuchaba en la penumbra sin hacer preguntas ni juzgarte. Era de los pocos que te hablaban como a una persona."));
        personas.add(new RecuerdoDePersona("La condenada que te perdonó", "Antes de que bajaras el hacha te dijo que no te odiaba. Nadie más volvió a decirte algo así."));
        personas.add(new RecuerdoDePersona("El carcelero", "Compartía su pan duro con vos sin decir una palabra. Era el único que entendía lo que pesaba el oficio."));
        return personas;
    }

    private ArrayList<Recuerdo> crearLugares() {
        ArrayList<Recuerdo> lugares = new ArrayList<>();
        lugares.add(new RecuerdoDeLugar("El hogar", "Una mesa con el pan todavía tibio, el fuego prendido en la chimenea y alguien que te esperaba despierto."));
        lugares.add(new RecuerdoDeLugar("La plaza del barrio", "Hamacas que chirriaban y un banco donde el tiempo no pasaba."));
        lugares.add(new RecuerdoDeLugar("El mar en verano", "Arena caliente, sal en la boca y el ruido de las olas de fondo."));
        lugares.add(new RecuerdoDeLugar("La biblioteca vieja", "Silencio, polvo y la luz de la tarde entrando por los vitrales."));
        lugares.add(new RecuerdoDeLugar("La cocina de la abuela", "Una olla al fuego desde temprano y la radio siempre prendida."));
        lugares.add(new RecuerdoDeLugar("La capilla vacía", "Bancos vacíos, olor a cera y un silencio donde por fin nadie te miraba. Rezabas antes de cada ejecución, aunque nunca supiste a quién."));
        lugares.add(new RecuerdoDeLugar("El cuarto compartido", "Una manta áspera, el murmullo de tus hermanos hablando bajito en la oscuridad y el calor de dormir todos juntos."));
        lugares.add(new RecuerdoDeLugar("El árbol del patio", "Una rama alta donde nadie te alcanzaba, el mundo chiquito allá abajo y toda la tarde por delante."));
        lugares.add(new RecuerdoDeLugar("El taller del maestro", "Olor a aceite, el filo del hacha en la piedra y una lámpara que ardía hasta tarde. Ahí aprendiste a hacer tu trabajo sin dejarte llevar por lo que sentían los condenados. Cuando salía bien, tu maestro asentía con la cabeza, y con eso alcanzaba."));
        return lugares;
    }

    private ArrayList<Recuerdo> crearTraumas() {
        ArrayList<Recuerdo> traumas = new ArrayList<>();
        traumas.add(new RecuerdoDeTrauma("La hoguera", "Ataste al condenado al poste y encendiste la leña. Todavía escuchás el crepitar cuando cerrás los ojos.", Elemento.FUEGO));
        traumas.add(new RecuerdoDeTrauma("El río helado", "Lo sostuviste bajo el agua hasta que dejó de moverse. El frío te quedó en las manos para siempre.", Elemento.AGUA));
        traumas.add(new RecuerdoDeTrauma("La jaula de la torre", "Lo dejaste encerrado en una jaula de hierro en lo alto de la torre. Esa noche hubo tormenta.", Elemento.RAYO));
        traumas.add(new RecuerdoDeTrauma("La copa", "Le serviste la copa y lo miraste beber. Sabía lo que era, y la bebió igual.", Elemento.VENENO));
        traumas.add(new RecuerdoDeTrauma("La pira del maestro", "Tu maestro, el verdugo que te enseñó todo, murió, y el pueblo quemó su cuerpo en la plaza entre burlas. Nadie lo lloró, salvo vos. Mirando las llamas supiste que a vos te espera lo mismo.", Elemento.FUEGO));
        traumas.add(new RecuerdoDeTrauma("El bautismo", "De niño viste cómo ahogaban a una \"bruja\" en el lago del pueblo mientras todos aplaudían. Ese día supiste que querías ser el que mata, y no el que mira.", Elemento.AGUA));
        traumas.add(new RecuerdoDeTrauma("La plaza bajo la lluvia", "Era una de tus primeras ejecuciones. El rayo cayó justo cuando bajabas el hacha, y el golpe salió torcido. Un verdugo mata limpio, sin una mancha, pero tuviste que golpear una y otra vez delante de toda la plaza. Esa noche te vieron como un asesino más.", Elemento.RAYO));
        traumas.add(new RecuerdoDeTrauma("El pozo del pueblo", "Alguien envenenó el pozo y tu único amigo, el que siempre te esperaba en la esquina, bebió primero. Ejecutaste al culpable despacio, sin apuro, y por primera vez disfrutaste cada segundo.", Elemento.VENENO));
        return traumas;
    }
}