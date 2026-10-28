package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class VentanaJuego extends JFrame {

    static final Color FONDO = new Color(0x15141C);
    static final Color PANEL = new Color(0x22202C);
    static final Color BOTON = new Color(0x2E2B3A);
    static final Color TEXTO = new Color(0xE6E1D3);
    static final Color TEXTO_SECUNDARIO = new Color(0x9A9386);
    static final Color DORADO = new Color(0xC9A25A);
    static final Color ROJO = new Color(0xB03A3A);

    private JLabel titulo;
    private JLabel narracion;
    private JPanel botones;
    private JPanel tarjetas;
    private ArrayList<TarjetaEnemigo> listaTarjetas;
    private PanelEstado panelEstado;

    public VentanaJuego() {
        super("El Limbo");
        setSize(980, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        listaTarjetas = new ArrayList<>();
        panelEstado = new PanelEstado();
        add(crearZona(), BorderLayout.CENTER);
        add(panelEstado, BorderLayout.EAST);
    }

    private JScrollPane crearZona() {
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);

        titulo = new JLabel("El Limbo");
        titulo.setFont(new Font(Font.SERIF, Font.BOLD, 26));
        titulo.setForeground(DORADO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        narracion = new JLabel();
        narracion.setFont(new Font(Font.SERIF, Font.ITALIC, 16));
        narracion.setForeground(TEXTO);
        narracion.setAlignmentX(Component.CENTER_ALIGNMENT);
        narracion.setMaximumSize(new Dimension(520, Integer.MAX_VALUE));

        botones = new JPanel();
        botones.setLayout(new BoxLayout(botones, BoxLayout.Y_AXIS));
        botones.setOpaque(false);
        botones.setAlignmentX(Component.CENTER_ALIGNMENT);
        botones.setMaximumSize(new Dimension(520, Integer.MAX_VALUE));

        tarjetas = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        tarjetas.setOpaque(false);
        tarjetas.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjetas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        tarjetas.setVisible(false);

        contenido.add(titulo);
        contenido.add(Box.createVerticalStrut(16));
        contenido.add(tarjetas);
        contenido.add(Box.createVerticalStrut(16));
        contenido.add(narracion);
        contenido.add(Box.createVerticalStrut(20));
        contenido.add(botones);

        JPanel zona = new JPanel(new BorderLayout());
        zona.setBackground(FONDO);
        zona.setBorder(BorderFactory.createEmptyBorder(56, 32, 24, 32));
        zona.add(contenido, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(zona);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getViewport().setBackground(FONDO);
        return scroll;
    }

    public PanelEstado getPanelEstado() {
        return panelEstado;
    }

    public void ponerZona(String texto) {
        titulo.setText(texto);
    }

    public void ponerPregunta(String texto, String[] opciones, ArrayBlockingQueue<Integer> respuestas) {
        narracion.setText("<html><div style='width:390px'>" + texto.replace("\n", "<br>") + "</div></html>");
        botones.removeAll();
        for (int i = 0; i < opciones.length; i++) {
            JButton boton = crearBoton(opciones[i]);
            int elegida = i;
            boton.addActionListener(e -> {
                apagarBotones();
                respuestas.offer(elegida);
            });
            botones.add(boton);
            botones.add(Box.createVerticalStrut(8));
        }
        botones.revalidate();
        botones.repaint();
    }

    public void ponerTarjetas(String[] nombres, String[] elementos, int[] vidas, int[] vidasMaximas) {
        tarjetas.removeAll();
        listaTarjetas.clear();
        for (int i = 0; i < nombres.length; i++) {
            TarjetaEnemigo tarjeta = new TarjetaEnemigo(i, nombres[i], elementos[i], vidas[i], vidasMaximas[i]);
            listaTarjetas.add(tarjeta);
            tarjetas.add(tarjeta);
        }
        tarjetas.setVisible(true);
        tarjetas.revalidate();
        tarjetas.repaint();
    }

    public void ocultarTarjetas() {
        tarjetas.setVisible(false);
    }

    public void pedirObjetivo(String texto, ArrayBlockingQueue<Integer> respuestas) {
        narracion.setText("<html><div style='width:390px'>" + texto.replace("\n", "<br>") + "</div></html>");
        botones.removeAll();
        botones.revalidate();
        botones.repaint();
        for (TarjetaEnemigo tarjeta : listaTarjetas) {
            tarjeta.activar(respuestas);
        }
    }

    private void desactivarTarjetas() {
        for (TarjetaEnemigo tarjeta : listaTarjetas) {
            tarjeta.desactivar();
        }
    }

    static Color colorDe(String elemento) {
        switch (elemento) {
            case "Fuego":
                return new Color(0xD9673A);
            case "Agua":
                return new Color(0x4A90C2);
            case "Rayo":
                return new Color(0xD8C14A);
            case "Veneno":
                return new Color(0x6FAF5A);
            default:
                return TEXTO_SECUNDARIO;
        }
    }

    static JButton crearBoton(String texto) {
        return crearBoton(texto, 16);
    }

    static JButton crearBoton(String texto, int tamañoLetra) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font(Font.SERIF, Font.PLAIN, tamañoLetra));
        boton.setForeground(TEXTO);
        boton.setBackground(BOTON);
        boton.setOpaque(true);
        boton.setFocusPainted(false);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DORADO),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, boton.getPreferredSize().height));
        return boton;
    }

    private void apagarBotones() {
        for (Component componente : botones.getComponents()) {
            componente.setEnabled(false);
        }
    }

    private class TarjetaEnemigo extends JPanel {

        private int posicion;
        private boolean enPie;
        private ArrayBlockingQueue<Integer> respuestas;

        TarjetaEnemigo(int posicion, String nombre, String elemento, int vida, int vidaMaxima) {
            this.posicion = posicion;
            this.enPie = vida > 0;
            this.respuestas = null;

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(BOTON);
            setPreferredSize(new Dimension(180, 84));
            ponerBorde(1, enPie ? DORADO : TEXTO_SECUNDARIO);

            JLabel etiquetaNombre = new JLabel(nombre);
            etiquetaNombre.setFont(new Font(Font.SERIF, Font.BOLD, 14));
            etiquetaNombre.setForeground(enPie ? TEXTO : TEXTO_SECUNDARIO);
            etiquetaNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel etiquetaElemento = new JLabel(elemento);
            etiquetaElemento.setFont(new Font(Font.SERIF, Font.PLAIN, 13));
            etiquetaElemento.setForeground(colorDe(elemento));
            etiquetaElemento.setAlignmentX(Component.LEFT_ALIGNMENT);

            JProgressBar barra = new JProgressBar(0, vidaMaxima);
            barra.setValue(vida);
            barra.setStringPainted(true);
            barra.setString(enPie ? vida + " / " + vidaMaxima : "Derrotada");
            barra.setForeground(ROJO);
            barra.setBackground(FONDO);
            barra.setAlignmentX(Component.LEFT_ALIGNMENT);
            barra.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));

            add(etiquetaNombre);
            add(Box.createVerticalStrut(2));
            add(etiquetaElemento);
            add(Box.createVerticalStrut(6));
            add(barra);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    elegir();
                }
            });
        }

        void activar(ArrayBlockingQueue<Integer> respuestas) {
            if (enPie) {
                this.respuestas = respuestas;
                ponerBorde(3, DORADO);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        }

        void desactivar() {
            this.respuestas = null;
            ponerBorde(1, enPie ? DORADO : TEXTO_SECUNDARIO);
            setCursor(Cursor.getDefaultCursor());
        }

        private void elegir() {
            if (respuestas != null) {
                ArrayBlockingQueue<Integer> cola = respuestas;
                desactivarTarjetas();
                cola.offer(posicion);
            }
        }

        private void ponerBorde(int grosor, Color color) {
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color, grosor),
                    BorderFactory.createEmptyBorder(8 - grosor, 10 - grosor, 8 - grosor, 10 - grosor)));
        }
    }
}