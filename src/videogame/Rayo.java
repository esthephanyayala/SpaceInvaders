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
public class Rayo extends Item{

    private Game game;
    private int speedY;// speed y
    
    public Rayo(int x, int y, int width, int height, int speedX, int speedY, 
            Game game) {
        super(x, y, width, height);
        this.speedY = speedY;
        this.game = game;
    }


    
    @Override
    public void tick() {
        // moving bar depending on keys <-  ->
        setY(getY() + getSpeedY());
        //collision with wall Y up
        if(getY() <=  0){
            int y =  game.getPlayer().getY() -  game.getPlayer().getHeight() ;
            int x =  game.getPlayer().getX() + (game.getPlayer().getWidth())/2;
            setY(y);
            setX(x);
            setSpeedY(0);
            game.setStarted(false);
        }
        
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Assets.rayo , getX(), getY(), getWidth(), getHeight(), null);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }
    
    
    
  

}
