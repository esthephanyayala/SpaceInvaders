/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videogame;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Esthephany Ayala Yañez
 * @author Alex Trujillo
 */
public class Borrego extends Item{

    private Game game;
    private int direction;// speed y
    
    public Borrego(int x, int y, int width, int height, int direction, int speedY, 
            Game game) {
        super(x, y, width, height);
        this.direction = speedY;
        this.game = game;
    }


    
    @Override
    public void tick() {
        // moving bar depending on keys <-  ->
        setX(getX() + getDirection()*2);
        //collision with wall Y up
        
        
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Assets.lamb , getX(), getY(), getWidth(), getHeight(), null);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
    
    
    
  

}
