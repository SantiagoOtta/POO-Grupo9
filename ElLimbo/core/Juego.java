package core;

import npcs.Mercader;
import recuerdos.Recuerdo;
import recuerdos.RecuerdoDeHabilidad;
import recuerdos.RecuerdoDeLugar;
import recuerdos.RecuerdoDePersona;
import recuerdos.RecuerdoDeTrauma;
import ui.Dialogos;

import java.awt.*;
import java.security.DigestException;
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
        Dialogos.zona("El Limbo");
        Dialogos.actualizarEstado(jugador);
        Dialogos.mostrar("Abris los ojos... Oscuridad... Silencio... la nada. \n\n"
                + "Recordas quien sos, o eras?. El Verdugo, las manos culpables de los asesinatos del rey.");
        Dialogos.mostrar("Esta nada es lo que conocemos como 'El Limbo', el espacio entre la vida y la muerte.\n\n"
                + "Si hay redención para alguien como vos tiene que encontrarse en algún lugar de esta oscuridad.");
        Dialogos.mostrar("A lo lejos comenzas a vislumbrar un diminuto pero esperanzador destello. Caminas para asegurarte de que no sea una ilusión.");
        elegirRecuerdosIniciales();
    }

    public void otorgarRecompensa(Personaje personaje) {

    }

    public Escena generarBosque() {
        return null;
    }

    public void guardarPartida() {

    }

    public boolean cargarPartida() {
        return false;
    }

    private void elegirRecuerdosIniciales() {
        Dialogos.zona("Tus recuerdos");
        Dialogos.mostrar("Entre mas te acercas mas claro lo ves. Burbujas flotantes? Pequeñas burbujas flotantes de distintos colores, no una, muchas de ellas.\n\n"
                + "Extrañado cual niño amagas a explotarlas hasta que observas figuras moviendose en el interior de cada una. Son tus recuerdos.");

        elegirUnoDe(crearHabilidades(), "Tres burbujas de color amarillo se acercan, como animandote a que las tomes.\n\n"
                + "Dentro de cada una ves el recuerdo de algo que sabías hacer:");
        Dialogos.mostrar("La burbuja se deshace en tus manos. Manos que recien te percatas que antes no sentias ni veias.\n"
                + "La oscuridad ya no es tan espesa.");

        elegirUnoDe(crearPersonas(), "Otras tres burbujas se aproximan, esta vez de color azul. Adentro hay caras que conocías:");
        Dialogos.mostrar("La oscuridad retrocede un poco más. Miras a tu alrededor sintiendote mas liviano, acostumbrandote al entorno.");

        elegirUnoDe(crearLugares(), "Tres burbujas más, verdes. Adentro hay lugares donde estuviste:");
        Dialogos.mostrar("Ya no es una oscuridad infinita: frente a vos se dibuja, por lo menos, un intento de camino.");

        elegirUnoDe(crearTraumas(), "Quedan tres burbujas, más oscuras que las demás, rojas. Ya sabes lo que hacer, aunque en este caso cuesta un poco más:");
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
        habilidades.add(new RecuerdoDeHabilidad("Hachazo ardiente", "El filo del hacha seprende fuego y corta quemando la carne.", Elemento.FUEGO));
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
        personas.add(new RecuerdoDePersona("El abrazo de la abuela", "Olía a pan y a jabón, y siempre duraba un poco más de lo necesario."));
        personas.add(new RecuerdoDePersona("El amigo del barrio", "Siempre te esperaba en la esquina, aunque llegaras tarde."));
        personas.add(new RecuerdoDePersona("La tutora", "Te enseñó a leer despacio, sin apurarte nunca."));
        personas.add(new RecuerdoDePersona("El hermano menor", "Te seguía a todos lados, y vos fingías que te molestaba."));
        return personas;
    }

    private ArrayList<Recuerdo> crearLugares() {
        ArrayList<Recuerdo> lugares = new ArrayList<>();
        lugares.add(new RecuerdoDeLugar("La plaza del barrio", "Hamacas que chirriaban y un banco donde el tiempo no pasaba."));
        lugares.add(new RecuerdoDeLugar("Tu hogar", "Una puerta que siempre estaba abierta para vos y una compañia indispensable."));
        lugares.add(new RecuerdoDeLugar("La biblioteca vieja", "Silencio, polvo y la luz de la tarde entrando por los vitrales."));
        lugares.add(new RecuerdoDeLugar("La cocina de la abuela", "Una olla al fuego desde temprano y la radio siempre prendida."));
        return lugares;
    }

    private ArrayList<Recuerdo> crearTraumas() {
        ArrayList<Recuerdo> traumas = new ArrayList<>();
        traumas.add(new RecuerdoDeTrauma("La hoguera", "Ataste al condenado al poste y encendiste la leña. Todavía escuchas el crepitar.", Elemento.FUEGO));
        traumas.add(new RecuerdoDeTrauma("El río helado", "Lo sostuviste bajo el agua hasta que dejó de moverse. El frio te quedó en las manos para siempre.", Elemento.AGUA));
        traumas.add(new RecuerdoDeTrauma("La jaula de la torre", "Lo dejaste encerrado en la soledad de la noche. Esa noche hubo tormenta.", Elemento.RAYO));
        traumas.add(new RecuerdoDeTrauma("La copa", "Le serviste la copa y lo miraste a los ojos mietras bebía. Amobs ya sabían el fin de ese trago.", Elemento.VENENO));
        return traumas;
    }

    private Elemento elementoAlAzar() {
        Elemento[] elementos = Elemento.values();
        return elementos[(int) (Math.random() * elementos.length)];
    }
}