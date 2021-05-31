/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package summative;


public class shot extends Character {
    boolean fired;
    boolean inRange;
    public shot(int x, int y, int speed) {
        super(x, y, speed);
        fired=false;
        inRange=false;
    }
    
}
