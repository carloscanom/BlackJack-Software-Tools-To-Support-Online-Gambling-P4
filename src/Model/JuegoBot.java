/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import GUI.WindowJuego;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Charlie
 */
public class JuegoBot {

    private Baraja baraja;
    private WindowJuego mw;
    private Maquina maq;
    private Perdedor perd;
    private int apuesta;

    public JuegoBot(WindowJuego mw) {
        this.mw = mw;
        this.maq = new Maquina();
        this.perd = new Perdedor(mw.getFichasIniciales());
        asignarLabelsPerd();
        asignarLabelsMaq();
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

    public WindowJuego getMw() {
        return mw;
    }

    public Maquina getMaq() {
        return maq;
    }

    public Perdedor getPerd() {
        return perd;
    }

    public int getApuesta() {
        return apuesta;
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
            mw.refrescarPuntuacionesBot();
        }
    }

    public Card sacarCarta() {
        Card c = baraja.getBaraja().get(0);
        baraja.getBaraja().remove(0);
        return c;
    }

    public void levantarCartaOcultaMaquinote() {
        maq.setPuntuacion(maq.getPuntuacion() + maq.getCartas().get(1).getValue());
        mw.refrescarPuntuacionesBot();
        mw.getjLabelCrupier2().setIcon(new ImageIcon(maq.getCartas().get(1).getRutaRep()));
    }

    public void jugada() {
        Card c;

        c = sacarCarta();
        mw.getjLabelJugador1().setIcon(new ImageIcon(c.getRutaRep()));
        perd.getCartas().add(c);
        perd.setPuntuacion(perd.getPuntuacion() + c.getValue());
        mw.refrescarPuntuacionesBot();

        c = sacarCarta();
        mw.getjLabelCrupier1().setIcon(new ImageIcon(c.getRutaRep()));
        maq.getCartas().add(c);
        maq.setPuntuacion(maq.getPuntuacion() + c.getValue());
        mw.refrescarPuntuacionesBot();

        c = sacarCarta();
        mw.getjLabelJugador2().setIcon(new ImageIcon(c.getRutaRep()));
        perd.getCartas().add(c);
        perd.setPuntuacion(perd.getPuntuacion() + c.getValue());
        mw.refrescarPuntuacionesBot();
        if (perd.getPuntuacion() == 21) {
            perd.setBlackjack(true);
        }

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
            } else if (maq.isBlackjack()) {
                ganaMaquina();
            } else if (perd.isBlackjack()) {
                ganaJug();
            }
            mw.getjButtonNuevaApuestaBot().setEnabled(true);
        }

        else {
            while (perd.getPuntuacion() < 12 || perd.getPuntuacion() < 18 && perd.getPuntuacion() < maq.getCartas().get(0).getValue() + 10) {
                Card x;
                x = sacarCarta();
                perd.getCartas().add(x);
                perd.getLabels().get(0).setIcon(new ImageIcon(x.getRutaRep()));
                perd.getLabels().remove(0);
                if (perd.getPuntuacion() + x.getValue() >= 21) {
                    if (perd.containsAce() != -1) {
                        perd.getCartas().remove(perd.containsAce());
                        perd.setPuntuacion(perd.getPuntuacion() - 10);
                    }
                }
                perd.setPuntuacion(perd.getPuntuacion() + x.getValue());
                mw.refrescarPuntuacionesBot();
            }

            if (!seHaPasado(perd)) {

                jugadaMaquinote();
                comprobarGanador();
            } else {
                levantarCartaOcultaMaquinote();
                ganaMaquina();
            }
            mw.getjButtonNuevaApuestaBot().setEnabled(true);
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

    public void apostarBot(int apuesta) {
        if (apuesta < perd.getFichas()) {
            baraja = new Baraja();
            this.apuesta = apuesta;
            perd.setFichas(perd.getFichas() - apuesta);
            mw.actualizarFichasBot();
            jugada();
        }
    }

    public boolean seHaPasado(Jugador jug) {
        return jug.getPuntuacion() > 21;
    }

    private void empate() {
        perd.setFichas(perd.getFichas() + apuesta);
        mw.actualizarFichasBot();
        JOptionPane.showMessageDialog(null,
                "Has empatado \n Partidas ganadas: " + "" + perd.getPartidasGanadas()
                + "\n Partidas perdidas: " + "" + maq.getPartidasGanadas(), "Algo es algo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void ganaMaquina() {
        maq.setPartidasGanadas(maq.getPartidasGanadas() + 1);
        JOptionPane.showMessageDialog(null,
                "Has perdido \n Partidas ganadas: " + "" + perd.getPartidasGanadas()
                + "\n Partidas perdidas: " + "" + maq.getPartidasGanadas(), "Ya pa' la próxima campeón",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void ganaJug() {
        perd.setPartidasGanadas(perd.getPartidasGanadas() + 1);
        if (perd.isBlackjack()) {
            perd.setFichas(perd.getFichas() + 3 * apuesta);
        } else {
            perd.setFichas(perd.getFichas() + 2 * apuesta);
        }
        mw.actualizarFichasBot();
        JOptionPane.showMessageDialog(null,
                "Has ganado \n Partidas ganadas: " + "" + perd.getPartidasGanadas()
                + "\n Partidas perdidas: " + "" + maq.getPartidasGanadas(), "Enhorabuena niño",
                JOptionPane.INFORMATION_MESSAGE);
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
