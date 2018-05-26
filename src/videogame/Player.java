/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videogame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Esthephany Ayala Yañez
 * @author Alex Trujillo
 */
public class Player extends Item{

    private Game game;
    public boolean color;
    
    public Player(int x, int y, int width, int height, Game game) {
        super(x, y, width, height);
        this.game = game;
        color = false;
    }

    @Override
    public void tick() {
        // moving player depending on keys <-  ->
        if (game.getKeyManager().left) {
           setX(getX() - 8);
        }
        if (game.getKeyManager().right) {
           setX(getX() + 8);
        }
        // collision with walls
        if (getX()+ getWidth() >= game.getWidth()) {
            setX(game.getWidth() - getWidth());
        }
        else if (getX() <= 0) {
            setX(0);
        }

    }

         private Rectangle getAboveBounds(){
        return new Rectangle (getX(), getY() + getHeight()/2 , getWidth(), getHeight() - getHeight()/2);
    }
    
      public boolean intersecsAbove(Object obj){
          return (obj instanceof Item && 
                this.getAboveBounds().intersects(((Item) obj).getBounds()));
     }
     
     public void setColor(){
         color = true;
     }
     
     
     
    @Override
    public void render(Graphics g) {
        g.drawImage(Assets.player, getX(), getY(), getWidth(), getHeight(), null);
    }
}
