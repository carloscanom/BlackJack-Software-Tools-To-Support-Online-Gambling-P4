/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import javax.swing.JLabel;

/**
 *
 * @author Charlie
 */
public class Jugador {
    
    private int puntuacion;
    private ArrayList<Card> cartas;
    private ArrayList<JLabel> labels;
    private int partidasGanadas;
    private boolean blackjack;
    
    public Jugador(){
        puntuacion = 0;
        cartas = new ArrayList<>();
        labels = new ArrayList<>();
        partidasGanadas = 0;
        blackjack = false;
    }

    public int getPuntuacion() {
        return puntuacion;
    }


    public ArrayList<Card> getCartas() {
        return cartas;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public ArrayList<JLabel> getLabels() {
        return labels;
    }

    public int containsAce(){ //Devuelve -1 si no hay as
        for(int i = 0; i < getCartas().size(); ++i){
            if(getCartas().get(i).getCharValue() == 'A' && getCartas().get(i).getValue() == 11) return i;
        }
        return -1;
    }

    public int getPartidasGanadas() {
        return partidasGanadas;
    }

    public void setPartidasGanadas(int partidasGanadas) {
        this.partidasGanadas = partidasGanadas;
    }

    public void setBlackjack(boolean blackjack) {
        this.blackjack = blackjack;
    }

    public boolean isBlackjack() {
        return blackjack;
    }

    public void setCartas(ArrayList<Card> cartas) {
        this.cartas = cartas;
    }

    public void setLabels(ArrayList<JLabel> labels) {
        this.labels = labels;
    }
    
    
}
