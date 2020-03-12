/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Charlie
 */
public class Perdedor extends Jugador{
    
    private int fichas;
    
    public Perdedor(int fichasIniciales){
        super();
        fichas = fichasIniciales;
    }

    public int getFichas() {
        return fichas;
    }

    public void setFichas(int fichas) {
        this.fichas = fichas;
    }

    
   
    
    
}
