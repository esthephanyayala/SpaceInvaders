/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videogame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import static java.lang.Integer.max;
import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Esthephany Ayala Yañez
 * @author Alex Trujillo
 */
public class Game implements Runnable {
    private BufferStrategy bs;      // to have several buffers when displaying
    private Graphics g;             // to paint objects
    private Display display;        // to display in the game
    String title;                   // title of the window
    private int width;              // width of the window
    private int height;             // height of the window
    private Thread thread;          // thread to create the game
    private boolean running;        // to set the game
    private boolean started;        // to start the game
    private boolean gameover;
    private Player player;          // to use a player
    private Rayo rayo; 
    private int vidas ;
    private boolean pause;
    private boolean lost;
    private ArrayList<Enemy> enemies; // Enemies (Aliens)
    private ArrayList<Fortaleza> fortalezas; // Fortalezas
    private KeyManager keyManager;  // to manage the keyboard
    private int score;
    private boolean win;
    private int cont;
    SoundClipTest sound;
    private int enemiesCont; //this allow us to count the enemies pause when tick
   private ArrayList<Bomba> bombas;
   private int tiempoBomba= (int)(Math.random()*50)+ 100; //cada cuantos segundos va a aparecer una bomba
   private int enemyElegido1,enemyElegido2;
   private int bombaX, bombaY;
   private int enemiesbombaCont, contadorbalas;
   private int enemyrandomX, enemyrandomY;
   private Borrego borrego;
   
  
    
    /**
     * to create title, width and height and set the game is still not running
     * @param title to set the title of the window
     * @param width to set the width of the window
     * @param height  to set the height of the window
     */
    public Game(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        running = false;
        started = false;
        gameover = false;
        pause = false;
        keyManager = new KeyManager();
        score = 0;
        lost = false;
        vidas = 3;
        win= false;
        enemiesCont = 0;
        cont=0;
        enemiesbombaCont=0;
        contadorbalas=0;
    }
    

    
    /**
     * initializing the display window of the game
     */
    private void init() {
         display = new Display(title, getWidth(), getHeight());  
         Assets.init();
         player = new Player(getWidth() / 2 - 50, getHeight() - 50, 100, 30, this);
         rayo = new Rayo(getWidth() / 2 - 10, player.y - player.height , 10, 30, 0, 0, this);
         borrego = new Borrego(-80, 60 , 50, 20, 0, 0, this);
         generateEnemies();
         generateFortalezas();
         bombas= new ArrayList<Bomba>();
         
         
         display.getJframe().addKeyListener(keyManager);
    }
    
    @Override
    public void run() {
        init();
        // frames per second
        int fps = 30;
        // time for each tick in nano segs
        double timeTick = 1000000000 / fps;
        // initializing delta
        double delta = 0;
        // define now to use inside the loop
        long now;
        // initializing last time to the computer time in nanosecs
        long lastTime = System.nanoTime();
        while (running) {
            // setting the time now to the actual time
            now = System.nanoTime();
            // acumulating to delta the difference between times in timeTick units
            delta += (now - lastTime) / timeTick;
            // updating the last time
            lastTime = now;
            // if delta is positive we tick the game
            if (delta >= 1) {
                tick();
                render();
                delta --;
            }
        }
      
        stop();
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }
    
    private void tick() {
        keyManager.tick();
       if(!win){
        if(!gameover){
            if(!lost){
                //To pause the game
                pause = this.getKeyManager().p;
                if(!(pause)){ //IF IS NOT PAUSED
                    // if space and game has not started
                    if (this.getKeyManager().space && !this.isStarted()) {
                        this.setStarted(true);
                        rayo.setSpeedY(-10);
                    } 
                    // moving player
                    player.tick();
                    borrego.tick();
                    // if game has started
                    if (this.isStarted()) {
                        // moving the rayo
                        rayo.tick();
                        
                    } else {
                        // moving the rayo based on the plaver
                        rayo.setX(player.getX() + player.getWidth() / 2 - rayo.getWidth() / 2);
                    }

                    Iterator itrb=bombas.iterator();
                   while (itrb.hasNext()){
                        
                        Bomba bomba1= (Bomba) itrb.next();
                        bomba1.tick();
                          //colision entre bomba y player 
                           if (bomba1.intersects(player)){
                               sound= new SoundClipTest("boom");
                               bombas.remove(bomba1);
                               itrb=bombas.iterator();
                               vidas--;
                               
                               resetPlayer();
                               resetRayo();
                              generateEnemies();
                              // System.out.println(vidas);
                           }
                           
                           //colision entre bomba y fortaleza
                           Iterator itrf=fortalezas.iterator();
                           while( itrf.hasNext()){
                               Fortaleza fortaleza= (Fortaleza) itrf.next();
                           if(bomba1.intersects(fortaleza)){
                               bombas.remove(bomba1);
                               itrb=bombas.iterator();


                               fortaleza.setVidas(fortaleza.getVidas()-1);

                               
                                if(fortaleza.getVidas() == 0){
                                    fortalezas.remove(fortaleza);
                                   itrf=fortalezas.iterator();
                                }           
                           }
                           }
                        //borrar cada bomba cada que llegue al suelo
                        if(bomba1.getY()>= getHeight()){
                           bombas.remove(bomba1);
                           itrb=bombas.iterator();
                         
                        }
                 }
                    
                    //enemigos se muevan
                    if(enemiesCont< 15){
                        Iterator itr= enemies.iterator();
                        while(itr.hasNext()){
                            ((Enemy)itr.next()).tick();
                            for(Enemy enemy: enemies){
                                //checa cuando hay colision a la derecha
                                if (enemy.getX()+enemy.getWidth() >= this.getWidth()){
                                    //agrupa a todos los enemigos como uno solo 
                                    for(Enemy enemy2: enemies){
                                        //se mueve para abajo y cambia de direccion
                                         enemy2.y=enemy2.y+1;
                                         enemy2.setX(enemy2.getX() - 1);
                                         enemy2.setDireccion(-1);
                                    } 
                                borregoToLeft();
                                }else if(enemy.getX() <= 0){//checa cuando hay colision a la izquierda
                                    for(Enemy enemy2: enemies){
                                        enemy2.y=enemy2.y+1;
                                        enemy2.setX(enemy2.getX() + 1);
                                        enemy2.setDireccion(1);
                                    }
                                borregoToRight();
                                }else if(enemy.getY() >= player.getY() - (player.getHeight() + rayo.getHeight())){
                                    gameover = true;
                                }
                            }    
                        }  
                    }
                    
                    
                    enemiesCont ++;
                    contadorbalas++;
                    
                          enemyElegido1=(int)(Math.random()*enemies.size())+1;
                                                   
                          //para mostrar las bombas en los enemigos 
                     Iterator itre= enemies.iterator();
                        while(itre.hasNext() && contadorbalas == 40){
                            Enemy enemy = (Enemy) itre.next();
                            
                            //System.out.println("contador de bombas"+ enemiesbombaCont);
                            //System.out.println("EnemyElegido"+enemyElegido1);
                            if(enemy!=null){
                            if(enemiesbombaCont==enemyElegido1 ){
                               //System.out.println(enemyElegido1);
                               bombas.add(new Bomba(enemy.getX()+enemy.getWidth()/2, enemy.getY(),10,30,0,10,this));
                               contadorbalas=0;
                               enemiesbombaCont=0;
                            }
                            else if(enemiesbombaCont==(enemyElegido1-1)){
                                bombas.add(new Bomba(enemy.getX()+enemy.getWidth()/2, enemy.getY(),10,30,0,10,this));
                               contadorbalas=0;
                               enemiesbombaCont=0;
                            }
                            else{
                               enemiesbombaCont++;
                               
                            }
                        
                        }
                        
                          // enemyElegido1=(int)(Math.random()*enemies.size())+1; 
                        }

                    
                   if(enemiesCont == 45){
                        enemiesCont = 0;
                    }
 
                    if(vidas == 0){
                        gameover = true;
                    }
                        
                    
                    // check collision enemies vs rayo
                    for (int i = 0; i < enemies.size(); i++) {
                        Enemy enemy = (Enemy) enemies.get(i);
                        if (enemy != null ){
                            if (rayo.intersects(enemy)) {
                                sound = new SoundClipTest("correct");
                                switch (enemy.getTipo()) {
                                    case 2:
                                        score+= 8;
                                        break;
                                    case 1:
                                        score+= 4;
                                        break;
                                    default:
                                        score+= 2;
                                        break;
                                }
                                
                                enemies.remove(enemy);
                               // enemyElegido1--;
                                i--;
                                int y =  getPlayer().getY() -  getPlayer().getHeight() ;
                                int x =  getPlayer().getX() + (getPlayer().getWidth())/2;
                                rayo.setY(y);
                                rayo.setX(x);
                                rayo.setSpeedY(0);
                                setStarted(false);   
                            }else if(rayo.intersects(borrego)){
                                int poderBorrego=(int)(Math.random()*(3));
                                
                                if(poderBorrego >=0 && poderBorrego <2){
                                    setScore(getScore()+ 100);
                                }
                                
                                if(poderBorrego >=2 && poderBorrego <2.5){
                                    setScore(getScore()+ 150);
                                }
                                if(poderBorrego >= 2.5 ){
                                    setScore (getScore()+200);
                                }
                                borrego.setX(-80);
                                borrego.setDirection(0);

                            }
                        }
                    }
                    
                     // check collision Fortaleza vs rayo
                    for (int i = 0; i < fortalezas.size(); i++) {
                        Fortaleza fortaleza = (Fortaleza) fortalezas.get(i);
                        if (fortaleza != null ){
                            if (rayo.intersects(fortaleza)) {
                                sound = new SoundClipTest("boom");
                                fortaleza.setVidas(fortaleza.getVidas() - 1);
                                if(fortaleza.getVidas() == 0){
                                    fortalezas.remove(fortaleza);
                                    i--;
                                }
                                int y =  getPlayer().getY() -  getPlayer().getHeight() ;
                                int x =  getPlayer().getX() + (getPlayer().getWidth())/2;
                                rayo.setY(y);
                                rayo.setX(x);
                                rayo.setSpeedY(0);
                                setStarted(false);  
                                
                            }  
                        }
                    }
                    
                    if(enemies.isEmpty()){
                         //resetGame(); ---> inside: generateEnemies();
                       // if(vidas < 6){
                        vidas = vidas  + 1;
                        generateEnemies();         
                   
                       // }
                }
            }
                

                else{
                    
                     getKeyManager().tick();
                    // if Wanna save 
                    if(this.getKeyManager().isS()){
                        //Como es estatico y no depende  de instancia  se manda a llamar la Clase (y solito se trae el obj)
                        Files.saveFile(this);
                    }
                    
                    // if Wanna load 
                    if(this.getKeyManager().isL()){
                        //Como es estatico y no depende  de instancia  se manda a llamar la Clase (y solito se trae el obj)
                        Files.loadFile(this);
                        this.getKeyManager().setL(false);
                    }
               //When game is LOST (live - 1), keymanager keeps listening for "J" ro init again
                if(this.getKeyManager().isJ()){
                    lost = false;
                    started = false;
                    resetRayo();
                    resetPlayer();
                } 
            }//END LOST****
        } 
                }else{
            //When GAMEOVER & WIN  keeps listening for "R" to reinit game
            if(this.getKeyManager().isR()){
                gameover = false;
                started = false;
                win = false;
                vidas = 3;
                score = 0;
                resetRayo();
                resetPlayer();
                generateEnemies();
            }
        }   

    }
    }
//END TICK();****
    private void drawWin(Graphics g){
        //show Win
        g.drawImage(Assets.win,(this.width / 2) - 200, (this.height / 2) - 200, 400 , 400, null);
    }
    
    private void drawGameOver(Graphics g){
       // Show Game Over
        g.drawImage(Assets.gameOver,(this.width / 2) - 200, (this.height / 2) - 200, 400 , 400, null);
    }
    
    private void drawLost(Graphics g){
       // Show LOST!!
        g.drawImage(Assets.lost, (this.width / 2) - 200, (this.height / 2) - 200, 400 , 400, null);
    }
    
     private void drawPause(Graphics g){
       // fShow LOST!!
        g.drawImage(Assets.pause, (this.width / 2) - 200, (this.height / 2) - 200, 400 , 400, null);
    }
    
    private void drawLives(Graphics g, int lnumber){
        
        if( lnumber == 6)
            g.drawImage(Assets.lives6, this.width - 240 , 15 , 150, 80, null);
        else if ( lnumber == 5)
            g.drawImage(Assets.lives5, this.width - 240 , 15 , 150, 80, null);
        else if ( lnumber == 4)
            g.drawImage(Assets.lives4, this.width - 240 , 15 , 150, 80, null);
        else if ( lnumber == 3)
            g.drawImage(Assets.lives3, this.width - 240 , 15 , 150, 80, null);
        else if ( lnumber == 2)
            g.drawImage(Assets.lives2, this.width - 240 , 15 , 150, 80, null);
        else if ( lnumber == 1)
            g.drawImage(Assets.lives1, this.width - 240 , 15 , 150, 80, null);
        else if ( lnumber <= 0)
            g.drawImage(Assets.livesNone, this.width - 240 , 15 , 150, 80, null);
    }

    
    private void drawScore(Graphics g){
        String a = Integer.toString(score);
        g.setColor(Color.BLACK);
        g.setFont(new Font ("arial",Font.PLAIN, 24));
 
        g.drawString("Unidades: " + a ,15, 25 + 15);
        
    }
    
    private void render() {
        // get the buffer strategy from the display
        bs = display.getCanvas().getBufferStrategy();
        /* if it is null, we define one with 3 buffers to display images of
        the game, if not null, then we display every image of the game but
        after clearing the Rectanlge, getting the graphic object from the 
        buffer strategy element. 
        show the graphic and dispose it to the trash system
        */
        if (bs == null) {
            display.getCanvas().createBufferStrategy(3);
        }else{
            g = bs.getDrawGraphics();
            g.drawImage(Assets.background, 0, 0, width, height, null);
            
            
            if(!gameover){
                player.render(g);
                borrego.render(g);
                rayo.render(g);
                
                for (Enemy brick : enemies) {
                    brick.render(g);
                    //bomba.render(g);
                }
                
                for (Fortaleza fortaleza : fortalezas) {
                    fortaleza.render(g);
                }
                
               for (Bomba bomba: bombas){
                    bomba.render(g);
               }
                
                drawScore(g);
                drawLives(g,vidas);
            }else if(gameover){
            drawGameOver(g);
            }
          

            if (lost && !gameover){
                drawLost(g);
            }
            if (pause && !lost && !gameover){
                drawPause(g);
            }
            if(win && !lost && !pause && !lost &&!gameover){
                
                drawWin(g);
            }
            
            bs.show();
            g.dispose();
        }
    }
    
    
    /**
     * setting the thead for the game
     */
    public synchronized void start() {
        if (!running) {
            running = true;
            thread = new Thread(this);
            thread.start();
        }
    }
    
    /**
     * stopping the thread
     */
    public synchronized void stop() {
        if (running) {
            running = false;
            try {
                thread.join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }           
        }
    }


    private void sleep() {
        try        
            {
                Thread.sleep(150);
            } 
            catch(InterruptedException ex) 
            {
                Thread.currentThread().interrupt();
            }
    }

    private void generateEnemies() {
        //Generate New Enemies
        enemies = new ArrayList<Enemy>();
        int width_enemy = 40;
        int height_enemy = 40;
        for (int i = 0; i < 10; i++) {
            for (int j = 1; j <= 5; j++) {
                //double randomNum = Math.random() * ( 3 );
                Enemy enemy = new Enemy(i * (width_enemy + 15) + 40 , j * (height_enemy + 15) + 40, width_enemy , height_enemy, this);
                if(j == 2 || j == 3 ) 
                    enemy.setTipo(1);
                if(j == 4 || j == 5 )
                    enemy.setTipo(2);
                enemies.add(enemy);
            }
        }
    }
    
    
    private void generateFortalezas() {
        //Generate New Fortalezas
        fortalezas = new ArrayList<Fortaleza>();
        int width_fortaleza = 120;
        int height_fortaleza = 90;
        for (int i = 1; i <= 3; i++) {
                double randomNum = Math.random() * ( 3 );
                Fortaleza fortaleza = new Fortaleza(i * ((getWidth() / 3)/2  + width_fortaleza/3  ) , (getHeight() - height_fortaleza - 120) , width_fortaleza , height_fortaleza, this);
                if(i == 2) 
                    fortaleza.setTipo(1);
                if(i == 3  )
                fortaleza.setTipo(2);
                fortalezas.add(fortaleza);
        }
    }
 

    private void resetPlayer() {
        player.setX(getWidth() / 2 - 50);
        player.setY(getHeight() - 50);
        player.setWidth(100);
    }

    private void resetRayo() {
        //Reset posicion og rayo and player
        rayo.setX(getWidth() / 2 - 10);
        rayo.setY(getHeight() - player.getHeight() - 50);
    }
    
    
    
    public BufferStrategy getBs() {
        return bs;
    }

    public void setBs(BufferStrategy bs) {
        this.bs = bs;
    }

    public Graphics getG() {
        return g;
    }

    public void setG(Graphics g) {
        this.g = g;
    }

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isGameover() {
        return gameover;
    }

    public void setGameover(boolean gameover) {
        this.gameover = gameover;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Rayo getRayo() {
        return rayo;
    }

    public void setRayo(Rayo rayo) {
        this.rayo = rayo;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public boolean isLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

     /**
     * To get the width of the game window
     * @return an <code>int</code> value with the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * To get the height of the game window
     * @return an <code>int</code> value with the height
     */
    public int getHeight() {
        return height;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
 
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public ArrayList<Fortaleza> getFortalezas() {
        return fortalezas;
    }

    public void setFortalezas(ArrayList<Fortaleza> fortalezas) {
        this.fortalezas = fortalezas;
    }

   // public boolean isWin() {
     //   return win;
    //}

    //public void setWin(boolean win) {        this.win = win;
    //}

    public int getCont() {
        return cont;
    }

    public void setCont(int cont) {
        this.cont = cont;
    }

    public SoundClipTest getSound() {
        return sound;
    }

    public void setSound(SoundClipTest sound) {
        this.sound = sound;
    }

    public int getEnemiesCont() {
        return enemiesCont;
    }

    public void setEnemiesCont(int enemiesCont) {
        this.enemiesCont = enemiesCont;
    }

    private void borregoToLeft() {
        borrego.setX(-50);
        borrego.setDirection(1);
    }

    private void borregoToRight() {
        borrego.setX(getWidth() + 50);
        borrego.setDirection(-1);
    }
    
    
}
