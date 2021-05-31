/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package summative;


public class Enemy extends Character {
    boolean moveRight;
    boolean moveLeft;
    boolean alive; //can be seen on screen
    public Enemy(int x, int y, int speed) {
        super(x, y, speed);
        moveRight=(true);//enemy starts moiving right
        moveLeft= (false);
        alive=(true); //all enemies are initially visable 
    }
    
}
