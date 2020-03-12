/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Launcher;

import GUI.WindowInicio;
import GUI.WindowJuego;
import javax.swing.SwingUtilities;

/**
 *
 * @author Charlie
 */
public class Main {
    
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WindowInicio().setVisible(true);
            }
        });
    }
    
}
