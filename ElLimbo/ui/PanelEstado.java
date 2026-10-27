package ui;

import javax.swing.*;
import java.awt.*;

public class PanelEstado extends JPanel {

    private JProgressBar barraVida;
    private JLabel datos;
    private JButton botonRecuerdos;
    private JLabel listaRecuerdos;
    private int cantidadRecuerdos;
    private JLabel aviso;
    private JButton botonGuardar;
    private JButton botonSalir;
    private JPanel confirmarSalida;
    private Runnable accionGuardar;

    public PanelEstado() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(VentanaJuego.PANEL);
        setPreferredSize(new Dimension(280, 0));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 0, 0, VentanaJuego.DORADO),
                BorderFactory.createEmptyBorder(24, 20, 24, 20)));

        JLabel nombre = new JLabel("EL VERDUGO");
        nombre.setFont(new Font(Font.SERIF, Font.BOLD, 18));
        nombre.setForeground(VentanaJuego.DORADO);
        nombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        barraVida = new JProgressBar(0, 100);
        barraVida.setValue(100);
        barraVida.setStringPainted(true);
        barraVida.setString("100 / 100");
        barraVida.setForeground(VentanaJuego.ROJO);
        barraVida.setBackground(VentanaJuego.BOTON);
        barraVida.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        barraVida.setAlignmentX(Component.LEFT_ALIGNMENT);

        datos = new JLabel();
        datos.setFont(new Font(Font.SERIF, Font.PLAIN, 14));
        datos.setForeground(VentanaJuego.TEXTO);
        datos.setAlignmentX(Component.LEFT_ALIGNMENT);

        botonRecuerdos = VentanaJuego.crearBoton("▸ Recuerdos (0)", 14);
        botonRecuerdos.addActionListener(e -> alternarRecuerdos());

        listaRecuerdos = new JLabel();
        listaRecuerdos.setFont(new Font(Font.SERIF, Font.PLAIN, 14));
        listaRecuerdos.setForeground(VentanaJuego.TEXTO_SECUNDARIO);
        listaRecuerdos.setAlignmentX(Component.LEFT_ALIGNMENT);
        listaRecuerdos.setVisible(false);

        aviso = new JLabel();
        aviso.setFont(new Font(Font.SERIF, Font.ITALIC, 13));
        aviso.setForeground(VentanaJuego.TEXTO_SECUNDARIO);
        aviso.setAlignmentX(Component.LEFT_ALIGNMENT);

        botonGuardar = VentanaJuego.crearBoton("Guardar partida", 14);
        botonGuardar.setEnabled(false);
        botonGuardar.addActionListener(e -> {
            if (accionGuardar != null) {
                accionGuardar.run();
            }
        });

        botonSalir = VentanaJuego.crearBoton("Salir del juego", 14);
        botonSalir.addActionListener(e -> mostrarConfirmacion(true));

        confirmarSalida = crearConfirmacion();
        confirmarSalida.setVisible(false);

        add(nombre);
        add(Box.createVerticalStrut(14));
        add(barraVida);
        add(Box.createVerticalStrut(14));
        add(datos);
        add(Box.createVerticalStrut(14));
        add(botonRecuerdos);
        add(Box.createVerticalStrut(6));
        add(listaRecuerdos);
        add(Box.createVerticalGlue());
        add(aviso);
        add(Box.createVerticalStrut(8));
        add(botonGuardar);
        add(Box.createVerticalStrut(8));
        add(botonSalir);
        add(confirmarSalida);
    }

    public void ponerEstado(int vida, int vidaMaxima, String textoDatos, String textoRecuerdos, int cantidad) {
        barraVida.setMaximum(vidaMaxima);
        barraVida.setValue(vida);
        barraVida.setString(vida + " / " + vidaMaxima);
        datos.setText(textoDatos);
        listaRecuerdos.setText(textoRecuerdos);
        cantidadRecuerdos = cantidad;
        actualizarBotonRecuerdos();
    }

    public void permitirGuardar(boolean permitido) {
        botonGuardar.setEnabled(permitido);
        aviso.setText("");
    }

    public void setAccionGuardar(Runnable accion) {
        this.accionGuardar = accion;
    }

    public void ponerAviso(String texto) {
        aviso.setText("<html><div style='width:180px'>" + texto + "</div></html>");
    }

    private JPanel crearConfirmacion() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel pregunta = new JLabel("¿Seguro que querés salir?");
        pregunta.setFont(new Font(Font.SERIF, Font.PLAIN, 14));
        pregunta.setForeground(VentanaJuego.TEXTO);
        pregunta.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton si = VentanaJuego.crearBoton("Sí, salir", 14);
        si.addActionListener(e -> System.exit(0));

        JButton no = VentanaJuego.crearBoton("No", 14);
        no.addActionListener(e -> mostrarConfirmacion(false));

        panel.add(pregunta);
        panel.add(Box.createVerticalStrut(6));
        panel.add(si);
        panel.add(Box.createVerticalStrut(6));
        panel.add(no);
        return panel;
    }

    private void mostrarConfirmacion(boolean mostrar) {
        botonSalir.setVisible(!mostrar);
        confirmarSalida.setVisible(mostrar);
        revalidate();
        repaint();
    }

    private void alternarRecuerdos() {
        listaRecuerdos.setVisible(!listaRecuerdos.isVisible());
        actualizarBotonRecuerdos();
        revalidate();
        repaint();
    }

    private void actualizarBotonRecuerdos() {
        String flecha = listaRecuerdos.isVisible() ? "▾" : "▸";
        botonRecuerdos.setText(flecha + " Recuerdos (" + cantidadRecuerdos + ")");
    }
}