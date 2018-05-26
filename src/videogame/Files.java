/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videogame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Esthephany Ayala Yañez
 * @author Alex Trujillo
 */
public class Files {
    
    
    public static void saveFile(Game game){
        //Define objects
        PrintWriter printWriter;
        
        //prueva este codigo por que puede tener una excepción
        try{
            //Creating file object (si ya esta creado lo reescribe o si no lo está, lo crea)
            printWriter = new PrintWriter (new FileWriter("data.txt"));
            //writing the game
            //¿Qué se gurdará? --> posición de player, vidas, puntos , enemigos
            //Se pone al principio "" +   para que el codigo lo trate como string 
            printWriter.println("" + game.getPlayer().getX() + "," +
                    game.getPlayer().getY());
            
            printWriter.println("" + game.getScore() + "," + game.getVidas());//Crash = vidas
            

          printWriter.println(""+game.getRayo().getX()+","+ game.getRayo().getY()+
                  ","+ game.getRayo().getSpeedY());
          
          printWriter.println(""+String.valueOf(game.isStarted()));
          
           
            printWriter.println("" + game.getEnemies().size());
            for(Enemy enemy1: game.getEnemies()){
                printWriter.println("" + enemy1.getX() + "," + enemy1.getY() +
                        "," + enemy1.getTipo() + ","+ enemy1.getVel() +
                        "," + enemy1.getDireccion());
            }
            //Enemy enemy = new Enemy(x ,y ,40, 40, tipo, vel,  direction, game );
            
          /*  printWriter.println(""+game.getFortalezas().size());
            for(Fortaleza fortaleza1: game.getFortalezas()){
               printWriter.println("" + fortaleza1.getVidas());
            }
            */
            
            printWriter.close(); //Cierrra para guardar
             
        } catch(IOException ioe){ //Qué pasaria si ezizte el error
            System.out.println("Error, se te llenó el disco Duro. " + ioe.toString()); //por estandar hay que desplegar lo que tiene el objeto
            
        }
    
    }
    
    public static void loadFile(Game game){
        BufferedReader bufferedReader;
        try{
            //Open File 
            bufferedReader = new BufferedReader(new FileReader("data.txt"));
            //get the first line
            String line = bufferedReader.readLine();
            // getting every token from the line
            String[] tokens = line.split(",");
            //defining x and y for player 
            game.getPlayer().setX(Integer.parseInt(tokens[0]));
            game.getPlayer().setY(Integer.parseInt(tokens[1]));
             
             //geting the nextLine
            line = bufferedReader.readLine();
            // getting every token from the line
            tokens = line.split(",");
            //defining score and lives (crash)
             game.setScore(Integer.parseInt(tokens[0]));
             game.setVidas(Integer.parseInt(tokens[1]));
             
             //get the first line
            line = bufferedReader.readLine();
            // getting every token from the line
            tokens = line.split(",");
            //defining x and y for the rayp 
            game.getRayo().setX(Integer.parseInt(tokens[0]));
            game.getRayo().setY(Integer.parseInt(tokens[1]));
            game.getRayo().setSpeedY(Integer.parseInt(tokens[2]));
             
            
            //get the first line
            line = bufferedReader.readLine();
            // getting every token from the line
            tokens = line.split(",");
              //geting the nextLine
              line = bufferedReader.readLine();
             
              game.setStarted(Boolean.valueOf(tokens[0]));
              
           
              
             int enemies  = Integer.parseInt(line);
             
             //clearing enemies
             game.getEnemies().clear(); //Borrar la lista de enemigos para crear los nuevos que están guardados
             //adding enemies
             for (int i = 0; i < enemies; i++) {
                 //getting next Line //geting the nextLine
                line = bufferedReader.readLine();
                // getting every token from the line
                tokens = line.split(",");
                int x = Integer.parseInt(tokens[0]);
                int y = Integer.parseInt(tokens[1]);
                int tipo = Integer.parseInt(tokens[2]);
                int vel =Integer.parseInt(tokens[3]);
                int direction = Integer.parseInt(tokens[4]);
                
            
                Enemy enemy = new Enemy(x ,y ,40, 40, tipo, vel,  direction, game );
                game.getEnemies().add(enemy);
               /* 
                int fortalezas = Integer.parseInt(line);
                //clear fortalezas
                game.getFortalezas().clear();
                //adding fortalezas
                for (int j = 0; j < fortalezas ; j++) {
                 //getting next Line //geting the nextLine
                line = bufferedReader.readLine();
                // getting every token from the line
                tokens = line.split(",");
                int vidas = Integer.parseInt(tokens[0]);
                
                } 
                */
                
                
            }
             
        } catch (IOException ioe){
            System.out.println("No hay datos que cargar. " + ioe.toString()); //por estandar hay que desplegar lo que tiene el objeto
        }
        
    }
    
    
}