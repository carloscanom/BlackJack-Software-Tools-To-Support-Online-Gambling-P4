/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import GUI.WindowJuego;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Charlie
 */
public class JuegoManual {

    private Baraja baraja;
    private WindowJuego mw;
    private Maquina maq;
    private Perdedor perd;
    private int apuesta;

    public JuegoManual(WindowJuego mw) {
        //baraja = new Baraja();
        this.mw = mw;
        this.maq = new Maquina();
        this.perd = new Perdedor(mw.getFichasIniciales());
        asignarLabelsPerd();
        asignarLabelsMaq();
        apuesta = 0;
        //comienzo();
    }

    public void comienzo() {

        Card c;

        c = sacarCarta();
        mw.getjLabelJugador1().setIcon(new ImageIcon(c.getRutaRep()));
        perd.getCartas().add(c);
        perd.setPuntuacion(perd.getPuntuacion() + c.getValue());
        mw.refrescarPuntuaciones();

        //esperar(1);
        c = sacarCarta();
        mw.getjLabelCrupier1().setIcon(new ImageIcon(c.getRutaRep()));
        maq.getCartas().add(c);
        maq.setPuntuacion(maq.getPuntuacion() + c.getValue());
        mw.refrescarPuntuaciones();

        //esperar(1);
        c = sacarCarta();
        mw.getjLabelJugador2().setIcon(new ImageIcon(c.getRutaRep()));
        perd.getCartas().add(c);
        perd.setPuntuacion(perd.getPuntuacion() + c.getValue());
        mw.refrescarPuntuaciones();
        if (perd.getPuntuacion() == 21) {
            perd.setBlackjack(true);
        }

        //esperar(1);
        c = sacarCarta();
        mw.getjLabelCrupier2().setIcon(new ImageIcon(getClass().getResource("/Imágenes/Repr Cartas/red_joker.jpg")));
        maq.getCartas().add(c);
        if (maq.getPuntuacion() + c.getValue() == 21) {
            maq.setBlackjack(true);
        }

        if (maq.isBlackjack() || perd.isBlackjack()) {
            levantarCartaOcultaMaquinote();
            if (maq.isBlackjack() && perd.isBlackjack()) {
                empate();
                mw.getjButtonNuevaApuesta().setVisible(true);
            } else if (maq.isBlackjack()) {
                ganaMaquina();
                mw.getjButtonNuevaApuesta().setVisible(true);
            } else if (perd.isBlackjack()) {
                ganaJug();
                mw.getjButtonNuevaApuesta().setVisible(true);
            }
        }

    }

    public void levantarCartaOcultaMaquinote() {
        maq.setPuntuacion(maq.getPuntuacion() + maq.getCartas().get(1).getValue());
        mw.refrescarPuntuaciones();
        mw.getjLabelCrupier2().setIcon(new ImageIcon(maq.getCartas().get(1).getRutaRep()));
    }

    public void jugadaMaquinote() {
        levantarCartaOcultaMaquinote();
        while (maq.getPuntuacion() < 17) {
            Card c;
            c = sacarCarta();
            maq.getCartas().add(c);
            maq.getLabels().get(0).setIcon(new ImageIcon(c.getRutaRep()));
            maq.getLabels().remove(0);
            if (maq.getPuntuacion() + c.getValue() > 21) {
                if (maq.containsAce() != -1) {
                    maq.getCartas().remove(maq.containsAce());
                    maq.setPuntuacion(maq.getPuntuacion() - 10);
                }
            }
            maq.setPuntuacion(maq.getPuntuacion() + c.getValue());
            mw.refrescarPuntuaciones();
        }
    }

    public static void esperar(int segundos) {
        try {
            Thread.sleep(segundos * 1000);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Card sacarCarta() {
        Card c = baraja.getBaraja().get(0);
        baraja.getBaraja().remove(0);
        return c;
    }

    public Baraja getBaraja() {
        return baraja;
    }

    public Maquina getMaq() {
        return maq;
    }

    public Perdedor getPerd() {
        return perd;
    }

    public void apostar(int apuesta) {
        if (apuesta < perd.getFichas()) {
            baraja = new Baraja();
            this.apuesta = apuesta;
            perd.setFichas(perd.getFichas() - apuesta);
            mw.actualizarFichas();
            comienzo();
        }
    }

    public boolean pedir() { //devuelve true si se ha pasado de 21
        Card c;
        boolean ret = false;
        c = sacarCarta();
        perd.getCartas().add(c);
        perd.getLabels().get(0).setIcon(new ImageIcon(c.getRutaRep()));
        perd.getLabels().remove(0);
        if (perd.getPuntuacion() + c.getValue() > 21) {
            if (perd.containsAce() != -1) {
                perd.getCartas().remove(perd.containsAce());
                perd.setPuntuacion(perd.getPuntuacion() - 10);
            }
        }
        perd.setPuntuacion(perd.getPuntuacion() + c.getValue());
        mw.refrescarPuntuaciones();
        if(perd.getPuntuacion() == 21){
            mw.desactivarPedirYPlantarse();
            jugadaMaquinote();
            comprobarGanador();
            mw.getjButtonNuevaApuesta().setVisible(true);
        }
        if (seHaPasado(perd)) {
            ret = true;
        }

        return ret;
    }

    public void asignarLabelsPerd() {
        perd.getLabels().add(mw.getjLabelJugador3());
        perd.getLabels().add(mw.getjLabelJugador4());
        perd.getLabels().add(mw.getjLabelJugador5());
        perd.getLabels().add(mw.getjLabelJugador6());
        perd.getLabels().add(mw.getjLabelJugador7());
        perd.getLabels().add(mw.getjLabelJugador8());
        perd.getLabels().add(mw.getjLabelJugador9());
        perd.getLabels().add(mw.getjLabelJugador10());
    }

    public void asignarLabelsMaq() {
        maq.getLabels().add(mw.getjLabelCrupier3());
        maq.getLabels().add(mw.getjLabelCrupier4());
        maq.getLabels().add(mw.getjLabelCrupier5());
        maq.getLabels().add(mw.getjLabelCrupier6());
        maq.getLabels().add(mw.getjLabelCrupier7());
        maq.getLabels().add(mw.getjLabelCrupier8());
        maq.getLabels().add(mw.getjLabelCrupier9());
        maq.getLabels().add(mw.getjLabelCrupier10());
    }

    public void ganaMaquina() {
        mw.desactivarPedirYPlantarse();
        maq.setPartidasGanadas(maq.getPartidasGanadas() + 1);
        JOptionPane.showMessageDialog(null,
                "Has perdido \n Partidas ganadas: " + "" + perd.getPartidasGanadas()
                + "\n Partidas perdidas: " + "" + maq.getPartidasGanadas(), "Ya pa' la próxima campeón",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void ganaJug() {
        mw.desactivarPedirYPlantarse();
        perd.setPartidasGanadas(perd.getPartidasGanadas() + 1);
        if (perd.isBlackjack()) {
            perd.setFichas(perd.getFichas() + 3 * apuesta);
        } else {
            perd.setFichas(perd.getFichas() + 2 * apuesta);
        }
        mw.actualizarFichas();
        JOptionPane.showMessageDialog(null,
                "Has ganado \n Partidas ganadas: " + "" + perd.getPartidasGanadas()
                + "\n Partidas perdidas: " + "" + maq.getPartidasGanadas(), "Enhorabuena niño",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void empate() {
        mw.desactivarPedirYPlantarse();
        perd.setFichas(perd.getFichas() + apuesta);
        JOptionPane.showMessageDialog(null,
                "Has empatado \n Partidas ganadas: " + "" + perd.getPartidasGanadas()
                + "\n Partidas perdidas: " + "" + maq.getPartidasGanadas(), "Algo es algo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean seHaPasado(Jugador jug) {
        return jug.getPuntuacion() > 21;
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

    public void nuevaApuesta() {
        perd.setPuntuacion(0);
        maq.setPuntuacion(0);

        maq.setLabels(new ArrayList<>());
        perd.setLabels(new ArrayList<>());
        asignarLabelsMaq();
        asignarLabelsPerd();

        perd.setCartas(new ArrayList<>());
        maq.setCartas(new ArrayList());

        perd.setBlackjack(false);
        maq.setBlackjack(false);
    }

}
