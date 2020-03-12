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
public final class GraficosJuntos {

    private final int numPartidas;
    private final Maquina maqBotTabla;
    private final Perdedor perdBotTabla;
    private final Maquina maqBotMio;
    private final Perdedor perdBotMio;
    private final Maquina maqBotPlantarse;
    private final Perdedor perdBotPlantarse;
    private final int fichasIniciales;
    private Baraja baraja;
    private int apuesta;
    //private int numEmpates;

    public GraficosJuntos(int numPartidas, int fichasIniciales) {
        this.numPartidas = numPartidas;
        this.fichasIniciales = fichasIniciales;
        maqBotTabla = new Maquina();
        this.perdBotTabla = new Perdedor(fichasIniciales);
        maqBotMio = new Maquina();
        this.perdBotMio = new Perdedor(fichasIniciales);
        maqBotPlantarse = new Maquina();
        this.perdBotPlantarse = new Perdedor(fichasIniciales);
        apuesta = 100;
        //numEmpates = 0;

        daleChicha();
    }

    public void daleChicha() {

        XYSeries botTabla = new XYSeries("BOT TABLA");
        XYSeries botMio = new XYSeries("BOT MIO");
        XYSeries botPlantarse = new XYSeries("PLANTARSE");

        int auxTabla = 0;

        for (int i = 0; i < numPartidas && perdBotTabla.getFichas() >= apuesta; ++i) {

            resetear(perdBotTabla, maqBotTabla);

            perdBotTabla.setFichas(perdBotTabla.getFichas() - apuesta);

            inicio(perdBotTabla, maqBotTabla);

            if (maqBotTabla.isBlackjack() || perdBotTabla.isBlackjack()) {
                levantarCartaOcultaMaquinote(maqBotTabla);
                if (maqBotTabla.isBlackjack() && perdBotTabla.isBlackjack()) {
                    empate(perdBotTabla);
                } else if (maqBotTabla.isBlackjack()) {
                    ganaMaquina(maqBotTabla);
                } else if (perdBotTabla.isBlackjack()) {
                    ganaJug(perdBotTabla);
                }
            } else {
                juegoBot3(perdBotTabla, maqBotTabla);

                if (!seHaPasado(perdBotTabla)) {
                    jugadaMaquinote(maqBotTabla);
                    comprobarGanador(perdBotTabla, maqBotTabla);
                } else {
                    ganaMaquina(maqBotTabla);
                }
            }

            botTabla.add(i + 1, perdBotTabla.getFichas());
            auxTabla++;
        }
        
        int numEmpatesTabla = auxTabla - (perdBotTabla.getPartidasGanadas() + maqBotTabla.getPartidasGanadas());

        hardReset();
        int auxMio = 0;

        for (int i = 0; i < numPartidas && perdBotMio.getFichas() >= apuesta; ++i) {

            resetear(perdBotMio, maqBotMio);

            perdBotMio.setFichas(perdBotMio.getFichas() - apuesta);

            inicio(perdBotMio, maqBotMio);

            if (maqBotMio.isBlackjack() || perdBotMio.isBlackjack()) {
                levantarCartaOcultaMaquinote(maqBotMio);
                if (maqBotMio.isBlackjack() && perdBotMio.isBlackjack()) {
                    empate(perdBotMio);
                } else if (maqBotMio.isBlackjack()) {
                    ganaMaquina(maqBotMio);
                } else if (perdBotMio.isBlackjack()) {
                    ganaJug(perdBotMio);
                }
            } else {
                juegoBotMio(perdBotMio, maqBotMio);

                if (!seHaPasado(perdBotMio)) {
                    jugadaMaquinote(maqBotMio);
                    comprobarGanador(perdBotMio, maqBotMio);
                } else {
                    ganaMaquina(maqBotMio);
                }
            }

            botMio.add(i + 1, perdBotMio.getFichas());
            auxMio++;
        }
        
        int numEmpatesMio = auxMio - (perdBotMio.getPartidasGanadas() + maqBotMio.getPartidasGanadas());

        hardReset();
        int auxPlantarse = 0;

        for (int i = 0; i < numPartidas && perdBotPlantarse.getFichas() >= apuesta; ++i) {

            resetear(perdBotPlantarse, maqBotPlantarse);

            perdBotPlantarse.setFichas(perdBotPlantarse.getFichas() - apuesta);

            inicio(perdBotPlantarse, maqBotPlantarse);

            if (maqBotPlantarse.isBlackjack() || perdBotPlantarse.isBlackjack()) {
                levantarCartaOcultaMaquinote(maqBotPlantarse);
                if (maqBotPlantarse.isBlackjack() && perdBotPlantarse.isBlackjack()) {
                    empate(perdBotPlantarse);
                } else if (maqBotPlantarse.isBlackjack()) {
                    ganaMaquina(maqBotPlantarse);
                } else if (perdBotPlantarse.isBlackjack()) {
                    ganaJug(perdBotPlantarse);
                }
            } else {
                juegoBot2(perdBotPlantarse);

                if (!seHaPasado(perdBotPlantarse)) {
                    jugadaMaquinote(maqBotPlantarse);
                    comprobarGanador(perdBotPlantarse, maqBotPlantarse);
                } else {
                    ganaMaquina(maqBotPlantarse);
                }
            }

            botPlantarse.add(i + 1, perdBotPlantarse.getFichas());
            auxPlantarse++;
        }
        
        int numEmpatesPlantarse = auxPlantarse - (perdBotPlantarse.getPartidasGanadas() + maqBotPlantarse.getPartidasGanadas());

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(botTabla);
        dataset.addSeries(botMio);
        dataset.addSeries(botPlantarse);

        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                "Información de las partidas",
                "NÚMERO DE PARTIDAS",
                "FICHAS",
                dataset,
                PlotOrientation.VERTICAL, true, true, false);

        XYPlot plot = xylineChart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.PINK);
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesPaint(2, Color.YELLOW);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));
        renderer.setSeriesStroke(2, new BasicStroke(2.0f));
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
            File af = new File(System.getProperty("user.dir") + "\\PDFs\\Info Partida Comparacion.pdf");
            int i = 1;
            while (af.exists()) {
                af = new File(System.getProperty("user.dir") + "\\PDFs\\Info Partida Comparacion" + i + ".pdf");
                i++;
            }
            String ruta = af.getAbsolutePath();
            PdfWriter pw = PdfWriter.getInstance(doc, new FileOutputStream(ruta));
            doc.open();

            Image imagen;
            imagen = Image.getInstance(System.getProperty("user.dir") + "\\Gráfico partidas.png");
            imagen.scaleAbsolute(600, 400);
            imagen.setAlignment(Element.ALIGN_CENTER);
            doc.add(imagen);

            Paragraph par = new Paragraph();
            Chunk ch = new Chunk();
            par.setAlignment(Element.ALIGN_LEFT);

            String bot = "\n \n".concat("Todas las apuestas realizadas son de 100 fichas. \n").concat("Se utilizan 3 barajas. \n")
                    .concat("La tabla de estrategias utilizada se adjunta en la siguiente hoja. \n");

            String botTablita = "\n BOT TABLA \n Nº de partidas jugadas: " + auxTabla + ".\n"
                    + "Nº de partidas ganadas: " + perdBotTabla.getPartidasGanadas() + ".\n"
                    + "Nº de partidas perdidas: " + maqBotTabla.getPartidasGanadas() + ".\n"
                    + "Nº de empates: " + numEmpatesTabla + ".\n"
                    + "Empezaste con " + this.fichasIniciales + " fichas y terminas con " + perdBotTabla.getFichas() + ". \n";

            String miBot = "\n BOT MIO \n Nº de partidas jugadas: " + auxMio + ".\n"
                    + "Nº de partidas ganadas: " + perdBotMio.getPartidasGanadas() + ".\n"
                    + "Nº de partidas perdidas: " + maqBotMio.getPartidasGanadas() + ".\n"
                    + "Nº de empates: " + numEmpatesMio + ".\n"
                    + "Empezaste con " + this.fichasIniciales + " fichas y terminas con " + perdBotMio.getFichas() + ". \n";

            String plantarseBot = "\n\n\n BOT PLANTARSE \n Nº de partidas jugadas: " + auxPlantarse + ".\n"
                    + "Nº de partidas ganadas: " + perdBotPlantarse.getPartidasGanadas() + ".\n"
                    + "Nº de partidas perdidas: " + maqBotPlantarse.getPartidasGanadas() + ".\n"
                    + "Nº de empates: " + numEmpatesPlantarse + ".\n"
                    + "Empezaste con " + this.fichasIniciales + " fichas y terminas con " + perdBotPlantarse.getFichas() + ".\n";

            ch.append(bot);
            ch.append(botTablita);
            ch.append(miBot);
            ch.append(plantarseBot);

            ch.setFont(new Font(Font.COURIER, 14, Font.NORMAL));
            par.add(ch);
            doc.add(par);

            Image imagen2;
            imagen2 = Image.getInstance(System.getProperty("user.dir") + "\\tabla estrategias.jpg");
            imagen2.setAlignment(Element.ALIGN_CENTER);
            doc.add(imagen2);

            doc.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(GraficoBot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | DocumentException ex) {
            Logger.getLogger(GraficoBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Card sacarCarta() {
        Card c = baraja.getBaraja().get(0);
        baraja.getBaraja().remove(0);
        return c;
    }

    public void levantarCartaOcultaMaquinote(Maquina maq) {
        maq.setPuntuacion(maq.getPuntuacion() + maq.getCartas().get(1).getValue());
    }

    public boolean seHaPasado(Jugador jug) {
        return jug.getPuntuacion() > 21;
    }

    private void empate(Perdedor perd) {
        perd.setFichas(perd.getFichas() + apuesta);
        //this.numEmpates++;
    }

    private void ganaMaquina(Maquina maq) {
        maq.setPartidasGanadas(maq.getPartidasGanadas() + 1);
    }

    private void ganaJug(Perdedor perd) {
        perd.setPartidasGanadas(perd.getPartidasGanadas() + 1);
        if (perd.isBlackjack()) {
            perd.setFichas(perd.getFichas() + 3 * apuesta);
        } else {
            perd.setFichas(perd.getFichas() + 2 * apuesta);
        }

    }

    public void jugadaMaquinote(Maquina maq) {
        levantarCartaOcultaMaquinote(maq);
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

    public void comprobarGanador(Perdedor perd, Maquina maq) {
        if (seHaPasado(perd)) {
            ganaMaquina(maq);
        } else if (seHaPasado(maq)) {
            ganaJug(perd);
        } else if (!seHaPasado(maq) && !seHaPasado(perd)) {
            if (maq.getPuntuacion() == perd.getPuntuacion()) {
                empate(perd);
            } else if (maq.getPuntuacion() > perd.getPuntuacion()) {
                ganaMaquina(maq);
            } else {
                ganaJug(perd);
            }
        }
    }

    private void resetear(Perdedor perd, Maquina maq) {
        this.baraja = new Baraja();

        perd.setPuntuacion(0);
        maq.setPuntuacion(0);

        perd.setCartas(new ArrayList<>());
        maq.setCartas(new ArrayList());

        perd.setBlackjack(false);
        maq.setBlackjack(false);

        this.apuesta = 100;

    }

    public void juegoBotMio(Perdedor perd, Maquina maq) {
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

    public void juegoBot2(Perdedor perd) {
        
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

    public void juegoBot3(Perdedor perd, Maquina maq) { //TABLA DE ESTRATEGIA
        //boolean asComprobado = false;
        boolean plantarse = false;

        while (!plantarse) {
            if (perd.containsAce() == -1 || perd.getCartas().size() > 2/*asComprobado*/) {
                //asComprobado = true;
                if (perd.getPuntuacion() >= 17
                        || perd.getPuntuacion() >= 13 && maq.getCartas().get(0).getValue() <= 6
                        || (perd.getPuntuacion() == 12 && maq.getPuntuacion() >= 4 && maq.getPuntuacion() <= 6)) {
                    plantarse = true;
                } else {
                    if (perd.getCartas().size() == 2 && perd.getFichas() >= 100) {
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
                        /*else if(maq.getPuntuacion() == 9 && perd.getPuntuacion() >= 5 && perd.getPuntuacion() <= 8){
                            perd.setFichas(perd.getFichas() - 100);
                            this.apuesta = 200;
                            haDoblado = true;
                        }*/
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
                    if (perd.getCartas().size() == 2 && perd.getFichas() >= 100) {
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

    public void hardReset() {
        apuesta = 100;
        //numEmpates = 0;
    }

    public void inicio(Perdedor perd, Maquina maq) {
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
    }

}
