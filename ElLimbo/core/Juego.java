package core;

import npcs.Mercader;
import recuerdos.Recuerdo;
import recuerdos.RecuerdoDeHabilidad;
import recuerdos.RecuerdoDeLugar;
import recuerdos.RecuerdoDePersona;
import recuerdos.RecuerdoDeTrauma;
import ui.Dialogos;

import java.util.ArrayList;
import java.util.Collections;

public class Juego {

    private Personaje jugador;
    private Escena escenaActual;
    private int puertasReveladas;
    private int encuentrosMercader;
    private Mercader mercader;

    public Juego() {
        this.jugador = new Personaje();
        this.escenaActual = null;
        this.puertasReveladas = 0;
        this.encuentrosMercader = 0;
        this.mercader = null;
    }

    public void iniciar() {
        Dialogos.alGuardar(() -> guardarPartida());
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
        menuCastillo();
        Dialogos.cerrar();
    }

    public void otorgarRecompensa(Personaje personaje) {

    }

    public Escena generarBosque() {
        return null;
    }

    public void guardarPartida() {
        Dialogos.avisar("El guardado se programa en el paso 21: todavía no se guarda nada.");
    }

    public boolean cargarPartida() {
        return false;
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

            ArrayList<String> opciones = new ArrayList<>();
            opciones.add("Salir al bosque");
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

            if (opcion.equals("Salir al bosque")) {
                Dialogos.zona("El bosque");
                ArrayList<Recuerdo> premio = crearPersonas();
                premio.add(new RecuerdoDePersona("La voz de mamá", "Te llamaba para entrar a casa. Nunca supiste si estaba enojada o asustada."));
                premio.add(new RecuerdoDeLugar("La casa de la infancia", "Un patio con baldosas rotas y una puerta que nunca cerraba bien."));
                Collections.shuffle(premio);
                jugador.agregarRecuerdo(premio.get(0));
                Dialogos.mostrar("(El bosque todavía no está programado: se arma en el paso 14. Por ahora te llevás "
                        + premio.get(0).getNombre() + " y volvés al castillo.)");
            } else if (opcion.equals("Acercarte a la puerta del ala este")) {
                puertaEste();
            } else if (opcion.equals("Acercarte a la puerta del ala oeste")) {
                puertaOeste();
            } else if (opcion.equals("Acercarte a la puerta enorme")) {
                puertaJefe();
            }
        }
    }

    private void puertaEste() {
        if (tieneRecuerdo("La casa de la infancia")) {
            Dialogos.mostrar("Apoyás la mano en la madera y te acordás de un patio con baldosas rotas y una puerta que nunca cerraba bien. "
                    + "Esta se abre sola.\n\n"
                    + "Adentro personificada esa habitacion que te acoompaño por tantos años."
                    + "hay una mesa, y sobre la mesa, una llave de hierro.");
            jugador.setTieneLlave(true);
            Dialogos.actualizarEstado(jugador);
            Dialogos.mostrar("Tomás la llave del castillo. Cuando te das vuelta, la puerta ya no está.");
        } else {
            Dialogos.mostrar("Te acercás a la puerta de madera gastada. Cuando la abris solo se ve la nada misma.\n\n"
                    + "Recuerdos, seguramente esa sea la clave. (Para entrar necesitás el Lugar \"La casa de la infancia\").");
        }
    }

    private void puertaOeste() {
        if (tieneRecuerdo("La voz de mamá")) {
            Dialogos.mostrar("La voz vuelve a llamarte para entrar. Esta vez la reconocés.\n\n"
                    + "La puerta se abre.");
            jugador.setTieneRecuerdoClave(true);
            Dialogos.actualizarEstado(jugador);
            Dialogos.mostrar("Adentro te espera un último recuerdo: El último grito. La última ejecución antes de tu muerte. "
                    + "El condenado gritó una sola palabra, y era tu nombre.\n\n"
                    + "Cuando salís, la puerta ya no está.");
        } else {
            Dialogos.mostrar("Te acercás a la puerta de madera del ala oeste, idéntica a la del ala este. "
                    + "Del otro lado se oye una voz, perdida en el fondo de tu alma.\n\n"
                    + "Como en la puerta este, necesita un recuerdo. (Para entrar necesitás la Persona \"La voz de mamá\").");
        }
    }

    private void puertaJefe() {
        if (jugador.isTieneLlave() && jugador.isTieneRecuerdoClave()) {
            boolean entrar = Dialogos.confirmar("La oscuridad se retira por completo. La puerta es enorme, de hierro negro, "
                    + "y del otro lado alguien te espera desde hace mucho.\n\n"
                    + "Tenés la llave y el recuerdo clave. ¿Entrás?");
            if (entrar) {
                Dialogos.mostrar("(El jefe final se programa mas adelante.)");
            }
        } else {
            String faltan = "";
            if (!jugador.isTieneLlave()) {
                faltan = faltan + "\n- la llave del castillo";
            }
            if (!jugador.isTieneRecuerdoClave()) {
                faltan = faltan + "\n- un recuerdo que todavía no tenés";
            }
            Dialogos.mostrar("Te acercás a la puerta enorme. La oscuridad no deja ver la mitad: solo distinguís una cerradura "
                    + "de lado izquierdo, del otro lado... probablemente un recuerdo.\n\n(Para abrirla te falta:" + faltan + ")");
        }
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

    private String descripcionCastillo() {
        if (puertasReveladas == 0) {
            return "El castillo esta envuelto en niebla. Apenas se distinguen muros y diagonales. "
                    + "Adentro no se ve nada.\n\nAfuera, el bosque espera.";
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

    private void elegirUnoDe(ArrayList<Recuerdo> opciones, String texto) {
        Collections.shuffle(opciones);
        StringBuilder narracion = new StringBuilder(texto);
        String[] botones = new String[3];
        for (int i = 0; i < 3; i++) {
            Recuerdo recuerdo = opciones.get(i);
            String nombre = recuerdo.getNombre();
            if (recuerdo instanceof RecuerdoDeHabilidad) {
                nombre = nombre + " (" + Dialogos.nombreElemento(((RecuerdoDeHabilidad) recuerdo).getElemento()) + ")";
            } else if (recuerdo instanceof RecuerdoDeTrauma) {
                nombre = nombre + " (" + Dialogos.nombreElemento(((RecuerdoDeTrauma) recuerdo).getElemento()) + ")";
            }
            botones[i] = nombre;
            narracion.append("\n\n").append(nombre).append(": ").append(recuerdo.getDescripcion());
        }
        int elegido = Dialogos.elegir(narracion.toString(), botones);
        jugador.agregarRecuerdo(opciones.get(elegido));
        Dialogos.actualizarEstado(jugador);
    }

    private ArrayList<Recuerdo> crearHabilidades() {
        ArrayList<Recuerdo> habilidades = new ArrayList<>();
        habilidades.add(new RecuerdoDeHabilidad("Hachazo ardiente", "El filo del hacha se prende fuego y corta quemando la carne.", Elemento.FUEGO));
        habilidades.add(new RecuerdoDeHabilidad("Lluvia de brasas", "Arrojás un puñado de brasas encendidas que se pegan a la piel del enemigo.", Elemento.FUEGO));
        habilidades.add(new RecuerdoDeHabilidad("Ahogamiento", "Una mano de agua helada le aprieta la garganta al enemigo hasta dejarlo sin aire.", Elemento.AGUA));
        habilidades.add(new RecuerdoDeHabilidad("Látigo de agua", "Un chorro de agua golpea como un látigo y abre la piel donde pega.", Elemento.AGUA));
        habilidades.add(new RecuerdoDeHabilidad("Golpe de tormenta", "Levantás el hacha al cielo y un rayo cae de lleno sobre el enemigo.", Elemento.RAYO));
        habilidades.add(new RecuerdoDeHabilidad("Relámpago", "Un relámpago corto salta de tu mano y atraviesa al enemigo de lado a lado.", Elemento.RAYO));
        habilidades.add(new RecuerdoDeHabilidad("Filo envenenado", "La hoja del hacha está untada en veneno, y cada corte sigue quemando por dentro.", Elemento.VENENO));
        habilidades.add(new RecuerdoDeHabilidad("Niebla tóxica", "Una nube verdosa envuelve al enemigo y le quema los pulmones al respirar.", Elemento.VENENO));
        return habilidades;
    }

    private ArrayList<Recuerdo> crearPersonas() {
        ArrayList<Recuerdo> personas = new ArrayList<>();
        personas.add(new RecuerdoDePersona("El abrazo de la abuela", "Olía a pan y a jabón, y duraba un poco más de lo necesario."));
        personas.add(new RecuerdoDePersona("El amigo del barrio", "Siempre te esperaba en la esquina, aunque llegaras tarde."));
        personas.add(new RecuerdoDePersona("La tutora", "Te enseñó a leer despacio, sin apurarte nunca."));
        personas.add(new RecuerdoDePersona("El hermano menor", "Te seguía a todos lados, y vos fingías que te molestaba."));
        return personas;
    }

    private ArrayList<Recuerdo> crearLugares() {
        ArrayList<Recuerdo> lugares = new ArrayList<>();
        lugares.add(new RecuerdoDeLugar("El hogar", "Una mesa con el pan todavía tibio, el fuego prendido en la chimenea y alguien que te esperaba despierto."));
        lugares.add(new RecuerdoDeLugar("La plaza del barrio", "Hamacas que chirriaban y un banco donde el tiempo no pasaba."));
        lugares.add(new RecuerdoDeLugar("El mar en verano", "Arena caliente, sal en la boca y el ruido de las olas de fondo."));
        lugares.add(new RecuerdoDeLugar("La biblioteca vieja", "Silencio, polvo y la luz de la tarde entrando por los vitrales."));
        lugares.add(new RecuerdoDeLugar("La cocina de la abuela", "Una olla al fuego desde temprano y la radio siempre prendida."));
        return lugares;
    }

    private ArrayList<Recuerdo> crearTraumas() {
        ArrayList<Recuerdo> traumas = new ArrayList<>();
        traumas.add(new RecuerdoDeTrauma("La hoguera", "Ataste al condenado al poste y encendiste la leña. Todavía escuchás el crepitar cuando cerrás los ojos.", Elemento.FUEGO));
        traumas.add(new RecuerdoDeTrauma("El río helado", "Lo sostuviste bajo el agua hasta que dejó de moverse. El frío te quedó en las manos para siempre.", Elemento.AGUA));
        traumas.add(new RecuerdoDeTrauma("La jaula de la torre", "Lo dejaste encerrado en una jaula de hierro en lo alto de la torre. Esa noche hubo tormenta.", Elemento.RAYO));
        traumas.add(new RecuerdoDeTrauma("La copa", "Le serviste la copa y lo miraste beber. Sabía lo que era, y la bebió igual.", Elemento.VENENO));
        return traumas;
    }
}