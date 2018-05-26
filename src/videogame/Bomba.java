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
public class Bomba extends Item {
     private Game game;
    private int speedY;// speed y
    
    public Bomba(int x, int y, int width, int height, int speedX, int speedY, 
            Game game) {
        super(x, y, width, height);
        this.speedY = 1;
        this.game = game;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    
    @Override
    public void tick() {
        
        setY(getY()+ 10);
        
    }
    
     @Override
    public void render(Graphics g) {
        g.drawImage(Assets.rayoEnemigo , getX(), getY(), getWidth(), getHeight(), null);
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
