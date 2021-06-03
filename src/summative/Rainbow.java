/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package summative;


public class Rainbow extends Character {
    boolean moveRight;
    boolean alive; //can be seen on screen
    public Rainbow(int x, int y, int speed) {
        super(x, y, speed);
        moveRight=(false);//enemy starts moiving right
        alive=(true); //all enemies are initially visable 
    }
    
}
