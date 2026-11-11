package ui;

import core.Elemento;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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
    private JButton botonTabla;
    private JDialog ventanaTabla;

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
        botonTabla = crearBotonTabla();
        botonTabla.addActionListener(e -> abrirTabla());

        JPanel encabezado = new JPanel();
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.X_AXIS));
        encabezado.setOpaque(false);
        encabezado.setAlignmentX(Component.LEFT_ALIGNMENT);
        encabezado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        encabezado.add(nombre);
        encabezado.add(Box.createHorizontalGlue());
        encabezado.add(botonTabla);

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

        add(encabezado);;
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

    private JButton crearBotonTabla() {
        JButton boton = new JButton(new ImageIcon(dibujarSimbolo()));
        boton.setToolTipText("Tabla de elementos");
        boton.setBackground(VentanaJuego.BOTON);
        boton.setOpaque(true);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(VentanaJuego.DORADO));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Dimension tamaño = new Dimension(28, 28);
        boton.setPreferredSize(tamaño);
        boton.setMinimumSize(tamaño);
        boton.setMaximumSize(tamaño);
        return boton;
    }

    private Image dibujarSimbolo() {
        BufferedImage imagen = new BufferedImage(18, 18, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imagen.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Elemento[] elementos = Elemento.values();
        int porcion = 360 / elementos.length;
        for (int i = 0; i < elementos.length; i++) {
            g.setColor(VentanaJuego.colorDe(Dialogos.nombreElemento(elementos[i])));
            g.fillArc(1, 1, 16, 16, 90 - i * porcion, -porcion);
        }
        g.setColor(VentanaJuego.BOTON);
        g.fillOval(6, 6, 6, 6);
        g.dispose();
        return imagen;
    }

    private void abrirTabla() {
        if (ventanaTabla == null) {
            ventanaTabla = crearVentanaTabla();
        }
        ventanaTabla.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
        ventanaTabla.setVisible(true);
    }

    private JDialog crearVentanaTabla() {
        JDialog ventana = new JDialog(SwingUtilities.getWindowAncestor(this), "Tabla de elementos");
        ventana.setUndecorated(true);

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(VentanaJuego.PANEL);
        contenido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VentanaJuego.DORADO),
                BorderFactory.createEmptyBorder(18, 22, 18, 22)));

        JLabel titulo = new JLabel("TABLA DE ELEMENTOS");
        titulo.setFont(new Font(Font.SERIF, Font.BOLD, 16));
        titulo.setForeground(VentanaJuego.DORADO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel tabla = new JLabel(textoTabla());
        tabla.setFont(new Font(Font.SERIF, Font.PLAIN, 15));
        tabla.setForeground(VentanaJuego.TEXTO);
        tabla.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton cerrar = VentanaJuego.crearBoton("Cerrar", 14);
        cerrar.addActionListener(e -> ventana.setVisible(false));

        contenido.add(titulo);
        contenido.add(Box.createVerticalStrut(12));
        contenido.add(tabla);
        contenido.add(Box.createVerticalStrut(14));
        contenido.add(cerrar);
        ventana.setContentPane(contenido);
        ventana.pack();
        return ventana;
    }

    private String textoTabla() {
        StringBuilder html = new StringBuilder("<html><div style='width:230px'>");
        for (Elemento elemento : Elemento.values()) {
            for (Elemento otro : Elemento.values()) {
                if (otro.debilidad() == elemento) {
                    html.append(conColor(elemento)).append(" es fuerte contra ").append(conColor(otro)).append("<br>");
                }
            }
        }
        html.append("<br><font color='#9a9386'><i>Pegarle a un enemigo con un elemento superior hace +50% de daño. "
                + "Lo mismo te pasa a vos con tu debilidad.</i></font></div></html>");
        return html.toString();
    }

    private String conColor(Elemento elemento) {
        String nombre = Dialogos.nombreElemento(elemento);
        Color color = VentanaJuego.colorDe(nombre);
        String hexa = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        return "<b><font color='" + hexa + "'>" + nombre + "</font></b>";
    }
}