package core;

import javax.swing.*;

public class EstadisticasDeRun {

    private int combatesGanados;
    private int monedasRecolectadas;
    private int recuerdosConseguidos;
    private int recuerdosPerdidos;
    private int bosquesRecorridos;
    private int disoluciones;

    public EstadisticasDeRun() {
        this(0, 0, 0, 0, 0, 0);
    }

    public EstadisticasDeRun(int combatesGanados, int monedasRecolectadas, int recuerdosConseguidos,
                             int recuerdosPerdidos,int bosquesRecorridos, int disoluciones) {
        this.combatesGanados = combatesGanados;
        this.monedasRecolectadas = monedasRecolectadas;
        this.recuerdosConseguidos  = recuerdosConseguidos;
        this.recuerdosPerdidos  = recuerdosPerdidos;
        this.bosquesRecorridos = bosquesRecorridos;
        this.disoluciones = disoluciones;
    }

    public void sumarCombate() {
        combatesGanados++;
    }

    public void sumarMonedas(int cantidad) {
        monedasRecolectadas += cantidad;
    }

    public void sumarRecuerdoConseguido() {
        recuerdosConseguidos++;
    }

    public void sumarRecuerdoPerdido() {
        recuerdosPerdidos++;
    }

    public void sumarBosque() {
        bosquesRecorridos++;
    }

    public void sumarDisolucion() {
        disoluciones++;
    }

    public int getCombatesGanados() {
        return combatesGanados;
    }

    public int getMonedasRecolectadas() {
        return monedasRecolectadas;
    }

    public int getRecuerdosConseguidos() {
        return recuerdosConseguidos;
    }

    public int getRecuerdosPerdidos() {
        return recuerdosPerdidos;
    }

    public int getBosquesRecorridos() {
        return bosquesRecorridos;
    }

    public int getDisoluciones() {
        return disoluciones;
    }

    public String resumen() {
        return "Resumen de la run\n\n"
                + "Combates ganados: " + combatesGanados + "\n"
                + "Monedas recolectadas: " + monedasRecolectadas + "\n"
                + "Recuerdos conseguidos: " + recuerdosConseguidos + "\n"
                + "Recuerdos perdidos: " + recuerdosPerdidos + "\n"
                + "Bosques recorridos: " + bosquesRecorridos + "\n"
                + "Disoluciones: " + disoluciones;
    }

    public String aLinea() {
        return combatesGanados + "|" + monedasRecolectadas + "|" + recuerdosConseguidos + "|"
                + recuerdosPerdidos + "|" + bosquesRecorridos + "|" + disoluciones;
    }

    public static EstadisticasDeRun deLinea(String linea) {
        String[] partes = linea.split("\\|");
        if (partes.length != 6) {
            throw new IllegalArgumentException("Faltan datos en las estadísticas.");
        }
        return new EstadisticasDeRun(Integer.parseInt(partes[0]), Integer.parseInt(partes[1]),
                Integer.parseInt(partes[2]), Integer.parseInt(partes[3]),
                Integer.parseInt(partes[4]), Integer.parseInt(partes[5]));

    }
}
