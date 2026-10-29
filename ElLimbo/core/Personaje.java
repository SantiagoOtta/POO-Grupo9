package core;

import excepciones.DisolucionException;
import excepciones.RecuerdoPerdidoException;
import recuerdos.Recuerdo;
import recuerdos.RecuerdoDeTrauma;
import recuerdos.Reutilizable;

import java.util.ArrayList;
import java.util.HashMap;

public class Personaje {

    private int vida;
    private int vidaMaxima;
    private int monedas;
    private ArrayList<Recuerdo> recuerdos;
    private Elemento debilidad;
    private Elemento resistenciaTemporal;
    private int porcentajeResistenciaTemporal;
    private HashMap<Elemento, Integer> resistencias;
    private boolean tieneLlave;
    private boolean tieneRecuerdoClave;
    private int bonusDaño;

    public Personaje() {
        this(100);
    }

    public Personaje(int vidaInicial) {
        this.vida = vidaInicial;
        this.vidaMaxima = vidaInicial;
        this.monedas = 0;
        this.recuerdos = new ArrayList<>();
        this.debilidad = null;
        this.resistenciaTemporal = null;
        this.porcentajeResistenciaTemporal = 0;
        this.resistencias = new HashMap<>();
        this.tieneLlave = false;
        this.tieneRecuerdoClave = false;
        this.bonusDaño = 0;
    }

    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getMonedas() {
        return monedas;
    }

    public int getBonusDaño() {
        return bonusDaño;
    }

    public Elemento getDebilidad() {
        return debilidad;
    }

    public boolean isTieneLlave() {
        return tieneLlave;
    }

    public boolean isTieneRecuerdoClave() {
        return tieneRecuerdoClave;
    }

    public void setTieneLlave(boolean tieneLlave) {
        this.tieneLlave = tieneLlave;
    }

    public void setTieneRecuerdoClave(boolean tieneRecuerdoClave) {
        this.tieneRecuerdoClave = tieneRecuerdoClave;
    }

    public HashMap<Elemento, Integer> getResistencias() {
        return new HashMap<>(resistencias);
    }

    public ArrayList<Recuerdo> getRecuerdos() {
        return new ArrayList<>(recuerdos);
    }

    public void setMonedas(int monedas) {
        this.monedas = monedas;
    }

    public void agregarRecuerdo(Recuerdo recuerdo) {
        int cantidadAntes = recuerdos.size();
        recuerdos.add(recuerdo);
        ajustarCrecimiento(cantidadAntes);
        if (recuerdo instanceof RecuerdoDeTrauma && debilidad == null) {
            debilidad = ((RecuerdoDeTrauma) recuerdo).getElemento();
        }
    }

    public void quitarRecuerdo(Recuerdo recuerdo) {
        int cantidadAntes = recuerdos.size();
        recuerdos.remove(recuerdo);
        ajustarCrecimiento(cantidadAntes);
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public Elemento getResistenciaTemporal() {
        return resistenciaTemporal;
    }

    public void setResistenciaTemporal(Elemento resistenciaTemporal) {
        this.resistenciaTemporal = resistenciaTemporal;
    }

    public int getPorcentajeResistenciaTemporal() {
        return porcentajeResistenciaTemporal;
    }

    public void setPorcentajeResistenciaTemporal(int porcentajeResistenciaTemporal) {
        this.porcentajeResistenciaTemporal = porcentajeResistenciaTemporal;
    }

    public void recibirDaño(int cantidad, Elemento elemento) {
        if (elemento != null) {
            if (elemento == debilidad) {
                cantidad = cantidad * 3 / 2;
            } else {
                int golpes = resistencias.getOrDefault(elemento, 0);
                if (golpes >= 3) {
                    cantidad = cantidad / 2;
                }
                resistencias.put(elemento, golpes + 1);
            }
            if (elemento == resistenciaTemporal) {
                cantidad = cantidad * (100 - porcentajeResistenciaTemporal) / 100;
            }
        }
        vida = vida - cantidad;
        if (vida < 0) {
            vida = 0;
        }
    }

    public void disolver() throws DisolucionException, RecuerdoPerdidoException {
        if (recuerdos.isEmpty()) {
            throw new RecuerdoPerdidoException("No te queda ningún recuerdo. Ya no queda nada de vos.");
        }
        int cantidadAntes = recuerdos.size();
        int posicion = (int) (Math.random() * recuerdos.size());
        Recuerdo perdido = recuerdos.remove(posicion);
        ajustarCrecimiento(cantidadAntes);
        vida = vidaMaxima;
        resistenciaTemporal = null;
        porcentajeResistenciaTemporal = 0;
        if (recuerdos.isEmpty()) {
            throw new RecuerdoPerdidoException("Te disolviste, y con vos se fue " + perdido.getNombre()
                    + ", tu último recuerdo. Ya no queda nada de vos.");
        }
        throw new DisolucionException("Tu cuerpo se deshace en la niebla. Cuando volvés a abrir los ojos, estás en el castillo, "
                + "entero otra vez. \n\nPero algo falta: " + perdido.getNombre() + " ya no está.");
    }

    public void usarRecuerdo(Recuerdo recuerdo) throws RecuerdoPerdidoException {
        recuerdo.activar(this);
        if (!(recuerdo instanceof Reutilizable)) {
            int cantidadAntes = recuerdos.size();
            recuerdos.remove(recuerdo);
            ajustarCrecimiento(cantidadAntes);
            if (recuerdos.isEmpty()) {
                throw new RecuerdoPerdidoException("Usaste tu último recuerdo. Ya no queda nada de vos.");
            }
        }
    }

    public void reiniciarResistencias() {
        resistencias.clear();
    }

    private void ajustarCrecimiento(int cantidadAntes) {
        int tramos = recuerdos.size() / 3 - cantidadAntes / 3;
        if (tramos != 0) {
            vida = vidaMaxima + tramos * 5;
            bonusDaño = bonusDaño + tramos * 2;
            if (tramos > 0) {
                vida = vida + tramos * 5;
            }
            if (vida > vidaMaxima) {
                vida = vidaMaxima;
            }
        }
    }
}