package ui;

import core.Elemento;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class VentanaJuego extends JFrame {

    static final Color FONDO = new Color(0x15141C);
    static final Color PANEL = new Color(0x22202C);
    static final Color BOTON = new Color(0x2E2B3A);
    static final Color BOTON_RESALTADO = new Color(0x3F3A52);
    static final Color TEXTO = new Color(0xE6E1D3);
    static final Color TEXTO_SECUNDARIO = new Color(0x9A9386);
    static final Color DORADO = new Color(0xC9A25A);
    static final Color DORADO_CLARO = new Color(0xF0D08C);
    static final Color SEPARADOR = new Color(0x4A4436);
    static final Color ROJO = new Color(0xB03A3A);
    static final Color ROJO_CLARO = new Color(0xE0705F);
    static final Color VERDE = new Color(0x7BC47F);

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
        UIManager.put("ToolTip.background", PANEL);
        UIManager.put("ToolTip.foreground", TEXTO);
        UIManager.put("ToolTip.border", BorderFactory.createLineBorder(DORADO));
        ToolTipManager.sharedInstance().setInitialDelay(150);
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
        ponerPregunta(texto, opciones, null, respuestas);
    }

    public void ponerPregunta(String texto, String[] opciones, String[] ayudas, ArrayBlockingQueue<Integer> respuestas) {
        narracion.setText("<html><div style='width:390px'>" + colorear(texto) + "</div></html>");        botones.removeAll();
        for (int i = 0; i < opciones.length; i++) {
            JButton boton = crearBoton(opciones[i]);
            if (ayudas != null && ayudas[i] != null) {
                boton.setToolTipText(ayudas[i]);
            }
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
        narracion.setText("<html><div style='width:390px'>" + colorear(texto) + "</div></html>");        botones.removeAll();
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
        boton.setBorder(bordeDeBoton(DORADO));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, boton.getPreferredSize().height));
        agregarResaltado(boton);
        return boton;
    }

    private static Border bordeDeBoton(Color color) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color),
                BorderFactory.createEmptyBorder(8, 16, 8, 16));
    }

    private static void agregarResaltado(JButton boton) {
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (boton.isEnabled()) {
                    boton.setBackground(BOTON_RESALTADO);
                    boton.setForeground(DORADO_CLARO);
                    boton.setBorder(bordeDeBoton(DORADO_CLARO));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(BOTON);
                boton.setForeground(TEXTO);
                boton.setBorder(bordeDeBoton(DORADO));
            }
        });
    }

    private static String colorear(String texto) {
        String resultado = texto;
        for (Elemento elemento : Elemento.values()) {
            String nombre = Dialogos.nombreElemento(elemento);
            resultado = resultado.replaceAll("\\b" + nombre + "\\b", resaltar(nombre, colorDe(nombre)));
        }
        resultado = resultado.replaceAll("(?<![+\\d])(\\d+) de daño", resaltar("$1 de daño", ROJO_CLARO));
        resultado = resultado.replaceAll("te cuesta (\\d+) de vida", "te cuesta " + resaltar("$1 de vida", ROJO_CLARO));
        resultado = resultado.replaceAll("Recuperás (\\d+) de vida", "Recuperás " + resaltar("$1 de vida", VERDE));
        resultado = resultado.replaceAll("(\\d+) monedas", resaltar("$1 monedas", DORADO_CLARO));
        return resultado.replace("\n", "<br>");
    }

    private static String resaltar(String texto, Color color) {
        return "<b><font color='" + hexa(color) + "'>" + texto + "</font></b>";
    }

    static String hexa(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
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