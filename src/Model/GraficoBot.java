/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Charlie
 */
public class GraficoBot {

    private final int numPartidas;
    private Maquina maq;
    private Perdedor perd;
    private final int fichasIniciales;
    private Baraja baraja;
    private int apuesta;
    private final int tipoBot;
    private int maximo;
    private int numEmpates;

    public GraficoBot(int numPartidas, int fichasIniciales, int tipoBot) {

        //Informacion
        this.numPartidas = numPartidas;
        this.fichasIniciales = fichasIniciales;
        maq = new Maquina();
        this.perd = new Perdedor(fichasIniciales);
        apuesta = 100;
        this.tipoBot = tipoBot;
        maximo = fichasIniciales;

        comenzar();
    }

    public void comenzar() {

        XYSeries fichas = new XYSeries("FICHAS");

        int aux = 0;
        
        for (int i = 0; i < numPartidas && perd.getFichas() >= apuesta; ++i) {

            resetear();

            perd.setFichas(perd.getFichas() - apuesta);

            Card c;

            c = sacarCarta();
            perd.getCartas().add(c);
            perd.setPuntuacion(perd.getPuntuacion() + c.getValue());

            c = sacarCarta();
            maq.getCartas().add(c);
            maq.setPuntuacion(maq.getPuntuacion() + c.getValue());

            c = sacarCarta();
            perd.getCartas().add(c);
            perd.setPuntuacion(perd.getPuntuacion() + c.getValue());
            if (perd.getPuntuacion() == 21) {
                perd.setBlackjack(true);
            }

            c = sacarCarta();
            maq.getCartas().add(c);
            if (maq.getPuntuacion() + c.getValue() == 21) {
                maq.setBlackjack(true);
            }

            if (maq.isBlackjack() || perd.isBlackjack()) {
                levantarCartaOcultaMaquinote();
                if (maq.isBlackjack() && perd.isBlackjack()) {
                    empate();
                } else if (maq.isBlackjack()) {
                    ganaMaquina();
                } else if (perd.isBlackjack()) {
                    ganaJug();
                }
            } else {
                switch (this.tipoBot) {
                    case 1:
                        juegoBotMio();
                        break;
                    case 2:
                        juegoBot2();
                        break;
                    case 3:
                        juegoBot3();
                        break;
                    default:
                        break;
                }
                if (!seHaPasado(perd)) {
                    jugadaMaquinote();
                    comprobarGanador();
                } else {
                    ganaMaquina();
                }
            }
            fichas.add(i + 1, perd.getFichas());
            aux++;
            if (perd.getFichas() > maximo) {
                maximo = perd.getFichas();
            }

        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(fichas);

        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                "Información de las partidas",
                "NÚMERO DE PARTIDAS",
                "FICHAS",
                dataset,
                PlotOrientation.VERTICAL, true, true, false);

        XYPlot plot = xylineChart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        plot.setRenderer(renderer);

        ChartPanel panel = new ChartPanel(xylineChart);

        // Creamos la ventana
        JFrame ventana = new JFrame("Grafica");
        ventana.setVisible(true);
        ventana.setSize(800, 600);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ventana.add(panel);
        ventana.setLocationRelativeTo(null);

        BufferedImage image = xylineChart.createBufferedImage(600, 400);
        try {
            ImageIO.write(image, "png", new File("Gráfico partidas.png"));
        } catch (IOException ex) {
            Logger.getLogger(GraficoBot.class.getName()).log(Level.SEVERE, null, ex);
        }

        Document doc = new Document();
        try {
            File af = new File(System.getProperty("user.dir") + "\\PDFs\\Info Partida.pdf");
            int i = 1;
            while (af.exists()) {
                af = new File(System.getProperty("user.dir") + "\\PDFs\\Info Partida " + i + ".pdf");
                i++;
            }
            String ruta = af.getAbsolutePath();
            PdfWriter pw = PdfWriter.getInstance(doc, new FileOutputStream(/*System.getProperty("user.dir") + "\\PDFs.pdf"*/ruta));
            doc.open();

            Image imagen;
            imagen = Image.getInstance(System.getProperty("user.dir") + "\\Gráfico partidas.png");
            imagen.scaleAbsolute(600, 400);
            imagen.setAlignment(Element.ALIGN_CENTER);
            doc.add(imagen);

            Paragraph par = new Paragraph();
            Chunk ch = new Chunk();
            par.setAlignment(Element.ALIGN_LEFT);

            String bot = "";
            switch (tipoBot) {
                case 1:
                    bot = "\n \n"
                            + "Este bot funciona de la siguiente manera: \n"
                            + "Todas las apuestas son de 100 fichas.\n"
                            + "Supondremos que la siguiente carta a salir tendrá valor 10, ya que es lo más probable (16 cartas).\n"
                            + "Siempre y cuando nuestra puntuación sea menor o igual a 11, pediremos carta.\n"
                            + "Siempre y cuando sea mayor o igual que 18, nos plantaremos.\n"
                            + "Si nuestra puntuación está comprendida entre 12 y 17 sumamos 10 al valor de la carta visible del crupier, "
                            + "si nuestra puntuación es mayor, nos plantamos, si no, pedimos otra carta. \n \n";
                    break;
                case 2:
                    bot = "\n \n"
                            + "Todas las apuestas son de 10 fichas. \n"
                            + "El jugador se planta nada más recibir sus dos cartas. \n \n";
                    break;
                case 3:
                    bot = "\n \n"
                            + "Todas las apuestas son de 10 fichas. \n"
                            + "Se utiliza una tabla de estrategia básica ya creada(Se adjunta más abajo). \n"
                            + "Se siguen los pasos que marca (pedir, plantarse o doblar) teniendo en cuenta nuestras cartas y la carta visible del crupier. \n \n";
                    break;
                default:
                    break;
            }

            this.numEmpates = aux - (perd.getPartidasGanadas() + maq.getPartidasGanadas());

            String datos = "Nº de partidas jugadas: " + aux + ".\n"
                    + "Nº de partidas ganadas: " + perd.getPartidasGanadas() + ".\n"
                    + "Nº de partidas perdidas: " + maq.getPartidasGanadas() + ".\n"
                    + "Nº de empates: " + numEmpates + ".\n"
                    + "Nº maximo de fichas alcanzadas: " + maximo + ".\n"
                    + "Empezaste con " + this.fichasIniciales + " fichas y terminas con " + perd.getFichas() + ".";

            ch.append(bot);
            ch.append(datos);
            ch.setFont(new Font(Font.COURIER, 14, Font.NORMAL));
            par.add(ch);
            doc.add(par);

            if (tipoBot == 3) {
                Image imagen2;
                imagen2 = Image.getInstance(System.getProperty("user.dir") + "\\tabla estrategias.jpg");
                imagen2.setAlignment(Element.ALIGN_CENTER);
                doc.add(imagen2);
            }

            doc.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(GraficoBot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GraficoBot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(GraficoBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Card sacarCarta() {
        Card c = baraja.getBaraja().get(0);
        baraja.getBaraja().remove(0);
        return c;
    }

    public void levantarCartaOcultaMaquinote() {
        maq.setPuntuacion(maq.getPuntuacion() + maq.getCartas().get(1).getValue());
    }

    public boolean seHaPasado(Jugador jug) {
        return jug.getPuntuacion() > 21;
    }

    private void empate() {
        perd.setFichas(perd.getFichas() + apuesta);
        this.numEmpates++;
    }

    private void ganaMaquina() {
        maq.setPartidasGanadas(maq.getPartidasGanadas() + 1);
    }

    private void ganaJug() {
        perd.setPartidasGanadas(perd.getPartidasGanadas() + 1);
        if (perd.isBlackjack()) {
            perd.setFichas(perd.getFichas() + 3 * apuesta);
        } else {
            perd.setFichas(perd.getFichas() + 2 * apuesta);
        }

    }

    public void jugadaMaquinote() {
        levantarCartaOcultaMaquinote();
        while (maq.getPuntuacion() < 17) {
            Card c;
            c = sacarCarta();
            maq.getCartas().add(c);
            if (maq.getPuntuacion() + c.getValue() > 21) {
                if (maq.containsAce() != -1) {
                    maq.getCartas().remove(maq.containsAce());
                    maq.setPuntuacion(maq.getPuntuacion() - 10);
                }
            }
            maq.setPuntuacion(maq.getPuntuacion() + c.getValue());
        }
    }

    public void comprobarGanador() {
        if (seHaPasado(perd)) {
            ganaMaquina();
        } else if (seHaPasado(maq)) {
            ganaJug();
        } else if (!seHaPasado(maq) && !seHaPasado(perd)) {
            if (maq.getPuntuacion() == perd.getPuntuacion()) {
                empate();
            } else if (maq.getPuntuacion() > perd.getPuntuacion()) {
                ganaMaquina();
            } else {
                ganaJug();
            }
        }
    }

    private void resetear() {
        this.baraja = new Baraja();

        perd.setPuntuacion(0);
        maq.setPuntuacion(0);

        perd.setCartas(new ArrayList<>());
        maq.setCartas(new ArrayList());

        perd.setBlackjack(false);
        maq.setBlackjack(false);

        this.apuesta = 100;

    }

    public void juegoBotMio() {
        while (perd.getPuntuacion() < 12 || perd.getPuntuacion() < 18 && perd.getPuntuacion() < maq.getCartas().get(0).getValue() + 10) {
            if (perd.getPuntuacion() >= 10 && perd.getPuntuacion() <= 11 && maq.getPuntuacion() <= 10 && perd.getCartas().size() == 2) {
                if (perd.getFichas() >= 100) {
                    apuesta *= 2;
                    perd.setFichas(perd.getFichas() - 100);
                }
            } else if (perd.containsAce() != -1 && perd.getCartas().size() == 2) {
                if (perd.getPuntuacion() <= 18 && maq.getPuntuacion() >= 5 && maq.getPuntuacion() <= 6) {
                    if (perd.getFichas() >= 100) {
                        apuesta *= 2;
                        perd.setFichas(perd.getFichas() - 100);
                    }
                }
            }
            Card x;
            x = sacarCarta();
            perd.getCartas().add(x);
            if (perd.getPuntuacion() + x.getValue() >= 21) {
                if (perd.containsAce() != -1) {
                    perd.getCartas().remove(perd.containsAce());
                    perd.setPuntuacion(perd.getPuntuacion() - 10);
                }
            }
            perd.setPuntuacion(perd.getPuntuacion() + x.getValue());
        }
    }

    public void juegoBot2() {
        if (perd.getPuntuacion() < 17) {
            Card x;
            x = sacarCarta();
            perd.getCartas().add(x);
            if (perd.getPuntuacion() + x.getValue() >= 21) {
                if (perd.containsAce() != -1) {
                    perd.getCartas().remove(perd.containsAce());
                    perd.setPuntuacion(perd.getPuntuacion() - 10);
                }
            }
            perd.setPuntuacion(perd.getPuntuacion() + x.getValue());
        }
    }

    public void juegoBot3() { //TABLA DE ESTRATEGIA
        boolean plantarse = false;

        while (!plantarse) {
            if (perd.containsAce() == -1 || perd.getCartas().size() > 2/*asComprobado*/) {
                //asComprobado = true;
                if (perd.getPuntuacion() >= 17
                        || perd.getPuntuacion() >= 13 && maq.getCartas().get(0).getValue() <= 6
                        || (perd.getPuntuacion() == 12 && maq.getPuntuacion() >= 4 && maq.getPuntuacion() <= 6)) {
                    plantarse = true;
                } else {
                    if (perd.getCartas().size() == 2) {
                        if (perd.getPuntuacion() == 11 && perd.getPuntuacion() != 11/*&& maq.getPuntuacion() != 11*/) {
                            perd.setFichas(perd.getFichas() - 100);
                            this.apuesta = 200;
                        } else if (perd.getPuntuacion() == 10 && maq.getPuntuacion() <= 9) {
                            perd.setFichas(perd.getFichas() - 100);
                            this.apuesta = 200;
                        } else if (perd.getPuntuacion() == 9 && maq.getPuntuacion() >= 3 && maq.getPuntuacion() <= 5) {
                            perd.setFichas(perd.getFichas() - 100);
                            this.apuesta = 200;
                        }
                    }
                    Card x;
                    x = sacarCarta();
                    perd.getCartas().add(x);
                    if (perd.getPuntuacion() + x.getValue() >= 21) {
                        if (perd.containsAce() != -1) {
                            perd.getCartas().remove(perd.containsAce());
                            perd.setPuntuacion(perd.getPuntuacion() - 10);
                        }
                    }
                    perd.setPuntuacion(perd.getPuntuacion() + x.getValue());
                }
            } else if (perd.getCartas().size() == 2) {
                if (perd.getPuntuacion() >= 19 || (perd.getPuntuacion() == 18 && (maq.getPuntuacion() == 2) || (maq.getPuntuacion() >= 7 && maq.getPuntuacion() <= 8))) {
                    plantarse = true;
                } else {
                    if (perd.getCartas().size() == 2) {
                        if (maq.getPuntuacion() == 6 || maq.getPuntuacion() == 5
                                || (maq.getPuntuacion() == 4 && perd.getPuntuacion() >= 15 && perd.getPuntuacion() <= 18)
                                || (maq.getPuntuacion() == 3 && perd.getPuntuacion() >= 17 && perd.getPuntuacion() <= 18)) {
                            this.apuesta = 200;
                            perd.setFichas(perd.getFichas() - 100);
                        }
                    }
                    Card x;
                    x = sacarCarta();
                    perd.getCartas().add(x);
                    if (perd.getPuntuacion() + x.getValue() >= 21) {
                        if (perd.containsAce() != -1) {
                            perd.getCartas().remove(perd.containsAce());
                            perd.setPuntuacion(perd.getPuntuacion() - 10);
                        }
                    }
                    perd.setPuntuacion(perd.getPuntuacion() + x.getValue());
                }
            }
        }

    }

    public String cartasJugador(Jugador jug) {
        String ret = "";
        for (int i = 0; i < jug.getCartas().size(); ++i) {
            ret += jug.getCartas().get(i).toString() + "";
        }
        return ret;
    }

}
