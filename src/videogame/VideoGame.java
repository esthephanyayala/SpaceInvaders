/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videogame;

/**
 *
 * @author Esthephany Ayala Yañez
 * @author Alex Trujillo
 */
public class VideoGame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Game g = new Game("Juego", 800, 720);
        g.start();
    }
    
}
