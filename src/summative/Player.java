/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package summative;


public class Player extends Character {
boolean moveRight;
boolean moveLeft;
    Player(int x, int y, int s) {
        super(x, y, s); //state the common variables as the character variables
        moveRight=(false);//players starts stationary 
        moveLeft= (false);
    }
    
}
