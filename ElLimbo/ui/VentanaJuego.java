package ui;

import javax.swing.*;
import java.awt.*;

public class VentanaJuego extends JFrame {

    static final Color FONDO = new Color(0x15141C);
    static final Color PANEL = new Color(0x22202C);
    static final Color BOTON = new Color(0x2E2B3A);
    static final Color TEXTO = new Color(0xE6E1D3);
    static final Color Texto_SECUNDARIO = new Color(0x9A9386);
    static final Color DORADO = new Color(0xC9A25A);
    static final Color ROJO = new Color(0xB03A3A);

    private JLabel titulo;
    private JLabel narracion;
    private JPanel botones;
    private JProgressBar barraVida;
    private JLabel datos;
    private JLabel recuerdos;

    public VentanaJuego() {
        super("El Limbo");
        setSize(980, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(crearZona(), BorderLayout.CENTER);
        add(crearEstado(), BorderLayout.EAST);
    }

    private JScrollPane crearZona() {
        JPanel zona = new JPanel();
        zona.setLayout(new BoxLayout(zona, BoxLayout.Y_AXIS));
        zona.setBackground(FONDO);
        zona.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        titulo = new JLabel("El Limbo");
        titulo.setFont(new Font(Font.SERIF, Font.BOLD, 26));
        titulo.setForeground(DORADO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        narracion = new JLabel();
        narracion.setFont(new Font(Font.SERIF, Font.ITALIC, 16));
        narracion.setForeground(TEXTO);
        narracion.setAlignmentX(Component.LEFT_ALIGNMENT);

        botones = new JPanel();
        botones.setFont(new Font(Font.SERIF, Font.ITALIC, 16));
        botones.setOpaque(false);
        botones.setAlignmentX(Component.LEFT_ALIGNMENT);

        zona.add(titulo);
        zona.add(Box.createVerticalStrut(16));
        zona.add(narracion);
        zona.add(Box.createVerticalStrut(20));
        zona.add(botones);

        JScrollPane scroll = new JScrollPane(zona);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(FONDO);
        return scroll;
    }

    private JPanel crearEstado() {
        JPanel estado = new JPanel();
        estado.setLayout(new BoxLayout(estado, BoxLayout.Y_AXIS));
        estado.setBackground(PANEL);
        estado.setPreferredSize(new Dimension(280, 0));
        estado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 0, 0, DORADO),
                BorderFactory.createEmptyBorder(24, 20, 24, 20)));

        JLabel nombre = new JLabel("EL VERDUGO");
        nombre.setFont(new Font(Font.SERIF, Font.BOLD, 18));
        nombre.setForeground(DORADO);
        nombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        barraVida = new JProgressBar(0, 100);
        barraVida.setValue(100);
        barraVida.setStringPainted(true);
        barraVida.setString("100 / 100");
        barraVida.setForeground(ROJO);
        barraVida.setBackground(BOTON);
        barraVida.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        barraVida.setAlignmentX(Component.LEFT_ALIGNMENT);

        datos = new JLabel();
        datos.setFont(new Font(Font.SERIF, Font.PLAIN, 14));
        datos.setForeground(TEXTO);
        datos.setAlignmentX(Component.LEFT_ALIGNMENT);

        recuerdos = new JLabel();
        recuerdos.setFont(new Font(Font.SERIF, Font.PLAIN, 14));
        recuerdos.setForeground(Texto_SECUNDARIO);
        recuerdos.setAlignmentX(Component.LEFT_ALIGNMENT);

        estado.add(nombre);
        estado.add(Box.createVerticalStrut(14));
        estado.add(barraVida);
        estado.add(Box.createVerticalStrut(14));
        estado.add(datos);
        estado.add(Box.createVerticalStrut(14));
        estado.add(recuerdos);
        return estado;
    }
}