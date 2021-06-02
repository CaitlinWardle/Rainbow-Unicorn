package summative;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.JPanel;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
//importing all libraries needed

public class Board  extends JPanel implements Runnable, MouseListener
{
//global variables (must exits everywhere) 
int lives= 5;
int score=0;
boolean ingame = true;
private Dimension d;
int BOARD_WIDTH=1000;
int BOARD_HEIGHT=500;
int x = 0;
BufferedImage unicornpic; //picture of playable character
BufferedImage sadpic;  //enemy sadaces
BufferedImage ball;  //ball fired when player shoots
BufferedImage tear; //enemy tears (bombs) 
BufferedImage RIP; //Picture Displayed If you die
private Thread animator;
Player user;// creating users with actions
shot shooting;
tear bomb;
Enemy [] army = new Enemy[24]; //creating enemy army of 24

    public Board()
    { 
        Random rand = new Random();
        int range = rand.nextInt(1000); 
        addKeyListener(new TAdapter());
        addMouseListener(this);
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
        setBackground(Color.black);
        user= new Player (BOARD_WIDTH/2,BOARD_HEIGHT-90,5); //screating a user at bottom of board
        shooting=new shot (user.x,BOARD_HEIGHT-5,5); //shot comes from user but start off Screen screen
        int enemyX=10;
        int enemyY=10; //enemy army starting location
        bomb= new tear (range,enemyY,5);
        for (int i=0; i<army.length; i++){
          bomb.dropped=true;
          army[i]= new Enemy (enemyX,enemyY,10);
          enemyX+=60; //creating row of enemy 50 units apart from each other
          if (i==11){ //new row after 12 enemy sad faces
              enemyX=45; //set new enemy row so it wont overlap
              enemyY+=40; //move second row down
          }
    }
        //set up all needed images
        try{
            tear=ImageIO.read(this.getClass().getResource ("tears.png"));
        }
        catch (IOException e){
            System.out.println ("Unreadable image");
        }
            if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
            }
        try{
            unicornpic=ImageIO.read(this.getClass().getResource ("unicorn.png"));
        }
        catch (IOException e){
            System.out.println ("Unreadable image");
        }
            if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
            }
        try{
            sadpic=ImageIO.read(this.getClass().getResource ("Sad.png"));
        }
        catch (IOException e){
            System.out.println ("Unreadable image");
        }
            if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
            }
         try{
            ball=ImageIO.read(this.getClass().getResource ("ball.jpg"));
        }
        catch (IOException e){
            System.out.println ("Unreadable image");
        }
            if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
            }
        try{
            RIP=ImageIO.read(this.getClass().getResource ("RIP.png"));
        }
        catch (IOException e){
            System.out.println ("Unreadable image");
        }
            if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
            }
        setDoubleBuffered(true);
    }
public void paint(Graphics g){
super.paint(g);

g.setColor(Color.white);
g.fillRect(0, 0, d.width, d.height); //set background colour to white
Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);
        g.setColor(Color.black);
        g.setFont(small);
        g.drawString("Score: "+score, 10, 10);
        g.drawString ("Lives: "+ lives,900,10); //set up score board and life count

//player
g.drawImage (unicornpic,user.x,user.y,50,50,null); //set the unicorn to be over user (visual of player) 
g.drawImage(ball,shooting.x,shooting.y,10,10,null);//set the shot image
 g.drawImage (tear,bomb.x,bomb.y,20,20,null); //set the bomb image
if (bomb.dropped==true){
             bomb.y+=5; //bombfalls
             if (bomb.y>500){ //once off screen
                 bomb.dropped=false;
             }
             if (user.x<=bomb.x && bomb.x<=user.x+50 && user.y==bomb.y){
               lives-=1; //is bomb hits player life is lost
             }
            }
if (bomb.dropped==false && lives!=0 && score!=240){
    Random rand = new Random(); //reset the bomb to fall again once last bomb is off screen
          int range = rand.nextInt(1000); 
          bomb.x=range; 
          bomb.y=army[20].y;
          bomb.dropped=true;
}
if (shooting.fired==true){
       shooting.y-=10; //shot goes straight up
    for (Enemy army1 : army) {
        if (army1.x - 1 <= shooting.x && shooting.x <= army1.x + 30 && army1.y - 1 <= shooting.y && shooting.y <= army1.y) {
            if (army1.alive==true){
                score+=10;
                shooting.y=800;
                shooting.x=900;  //set the ball to be invisable (off of visible screen) 
                shooting.fired=false; //reset
                army1.alive = false; //if you hit enemy they die
            }
        }
    }
}
if (user.moveRight==true){
    user.x+=user.speed; //move right
}
if (user.moveLeft==true){ 
    user.x-=user.speed; //move left
}
//keeps player on board 
if (user.x>940){
       user.x-=user.speed;
         }
if (user.x<0){
     user.x+=user.speed;
}
moveEnemy(); 
    for (Enemy army1 : army) {
        if (army1.alive == true) {
            //only draw enemy if it remains unhit by player
            g.drawImage(sadpic, army1.x, army1.y, 30, 30, null);
        }
    }
    if (ingame) {
       
    }
if (lives<=0){
    g.drawImage (RIP,user.x,user.y-50,100,100,null); //display death image
    for (Enemy army1 : army) {
        //stop army
        army1.moveLeft = false;
        army1.moveRight = false;
        g.setColor(Color.black); //display back screen
        Font big = new Font("Helvetica", Font.BOLD, 35);
        FontMetrics m = this.getFontMetrics(big);
        g.fillRect(300,50,400,300);
        g.setColor(Color.white);
        g.setFont(big);
        g.drawString("YOU  DIED", 400, 80);
    }
}
if (score>=240){
    for (Enemy army1 : army) {
        //stop army
        army1.moveLeft = false;
        army1.moveRight = false;
        g.setColor(Color.black); //display back screen
        Font big = new Font("Helvetica", Font.BOLD, 35);
        FontMetrics m = this.getFontMetrics(big);
        g.fillRect(300,50,400,300);
        g.setColor(Color.white);
        g.setFont(big);
        g.drawString("YOU  WON!!!", 400, 80);
    }
}
Toolkit.getDefaultToolkit().sync();
g.dispose();
}
public void moveEnemy(){
    for (Enemy army1 : army) {
        if (army1.moveLeft == true) {
            army1.x -= 2; //army shifts left
        }
        if (army1.moveRight == true) {
            army1.x += 2; //army shifts right
        }
    }
    for (Enemy army2 : army) {
        if (army2.x > BOARD_WIDTH) {
            for (Enemy army1 : army) {
                army1.moveLeft = true; //when edge of screen hit change directions
                army1.moveRight = false;
                army1.y += 5; //when army hits edge of screen move down
            }
        }
        if (army2.x < 0) {
            for (Enemy army1 : army) {
                army1.moveRight = true; //when edge of screen hit change directions
                army1.moveLeft = false;
                army1.y += 5;
            }
        }
    }
}

    private void dispose() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
private class TAdapter extends KeyAdapter {
public void keyReleased(KeyEvent e) {
     int key = e.getKeyCode();
     user.moveRight=false;
     user.moveLeft=false;     //stop movement once key released
}
public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode(); //find the key that was pressed
        if(key==39 && lives>0){//when right arrow key is pressed
          user.moveRight=true; 
        }
        if (key==37 && lives>0){
          user.moveLeft=true; //when left arrow key is pressed
        }
        if (key==32 && lives>0){
          shooting.y=BOARD_HEIGHT-100;
          shooting.x=user.x; 
          shooting.fired=true;//when space key pressed fire shot
        }
}   
}
public void mousePressed(MouseEvent e) {
    int x = e.getX();
     int y = e.getY();
}
public void mouseReleased(MouseEvent e) {
}
public void mouseEntered(MouseEvent e) {
}

public void mouseExited(MouseEvent e) {
}
public void mouseClicked(MouseEvent e) {

}
public void run() {
long beforeTime, timeDiff, sleep;

beforeTime = System.currentTimeMillis();
 int animationDelay = 15;
 long time = 
            System.currentTimeMillis();
    while (true) {//infinite loop
     // spriteManager.update();
      repaint();
      try {
        time += animationDelay;  //delay runtime
        Thread.sleep(Math.max(0,time - 
          System.currentTimeMillis()));
      }catch (InterruptedException e) {
        System.out.println(e);
      }//end catch
    }//end while loop
}//end of run

}//end of class

         
  
