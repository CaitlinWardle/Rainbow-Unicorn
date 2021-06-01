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
int lives= 3;
int score=0;
boolean ingame = true;
private Dimension d;
int BOARD_WIDTH=1000;
int BOARD_HEIGHT=500;
int x = 0;
int enemyY;
BufferedImage unicornpic; //picture of playable character
BufferedImage sadpic;  //enemy sadaces
BufferedImage ball;  //ball fired when player shoots
BufferedImage tear; //enemy tears (bombs) 
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
        user= new Player (BOARD_WIDTH/2,BOARD_HEIGHT-90,5);
        shooting=new shot (user.x,BOARD_HEIGHT-100,5); 
        int enemyX=10;
        enemyY=10;
        bomb= new tear (range,enemyY,5);
        for (int i=0; i<army.length; i++){
          bomb.dropped=true;
          army[i]= new Enemy (enemyX,enemyY,10);
          enemyX+=60; //creating row of enemy 50 units apart from each other
          if (i==11){ //new row after 12 enemy sad faces
              enemyX=45; //set starting enemy back to original x position
              enemyY+=40; 
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
        g.drawString ("Lives: "+ lives,900,10);

//player
g.drawImage (unicornpic,user.x,user.y,50,50,null); //set the unicorn to be over user (visual of player) 
g.drawImage(ball,shooting.x,shooting.y,10,10,null);
 g.drawImage (tear,bomb.x,bomb.y,20,20,null);
if (bomb.dropped==true){
             bomb.y+=5; 
             if (bomb.y>500){
                 bomb.dropped=false;
             }
             if (user.x<=bomb.x && bomb.x<=user.x+50 && user.y==bomb.y){
               lives-=1; 
             }
            }
if (bomb.dropped==false){
    Random rand = new Random(); //everytime you fire enemy fires
          int range = rand.nextInt(1000); 
          bomb.x=range; 
          bomb.y=enemyY;
          bomb.dropped=true;
}
if (shooting.fired==true){
       shooting.y-=10;
       for (int i=0; i<army.length; i++){
       if (army[i].x<=shooting.x&& shooting.x<=army[i].x+30&& army[i].y<=shooting.y && shooting.y<=army[i].y){
        army[i].alive=false;
        score+=10;
        shooting.y=800;
        shooting.x=900;  //set the ball to be invisable (off of visible screen) 
        shooting.fired=false; 
       }
       }
    }
if (user.moveRight==true){
    user.x+=user.speed;
}
if (user.moveLeft==true){ 
    user.x-=user.speed; 
}
//keeps player on board 
if (user.x>940){
       user.x-=user.speed;
         }
if (user.x<0){
     user.x+=user.speed;
}
moveEnemy(); 
for (int i=0; i<army.length; i++){
    if (army[i].alive==true){
        g.drawImage (sadpic, army[i].x, army[i].y, 30, 30,null); 
       }
}
    if (ingame) {
       
    }
Toolkit.getDefaultToolkit().sync();
g.dispose();
}
public void moveEnemy(){
    for (int i=0; i<army.length; i++){
    if (army[i].moveLeft==true){
        army[i].x-=2; 
    }
    if (army[i].moveRight==true){
        army[i].x+=2; 
    }
    }
    for (int i=0; i<army.length; i++){
    if (army[i].x>BOARD_WIDTH){
      for (int n=0; n<army.length; n++){
        army[n].moveLeft=true;
        army[n].moveRight=false; 
        army[n].y+=5; //when army hits edge of screen move down
         }
    }
    if (army[i].x<0){
      for (int n=0; n<army.length; n++){
        army[n].moveRight=true;
        army[n].moveLeft=false; 
        army[n].y+=5;
         }
    }
}
}
private class TAdapter extends KeyAdapter {
public void keyReleased(KeyEvent e) {
     int key = e.getKeyCode();
     user.moveRight=false;
     user.moveLeft=false;     
}
public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode(); //find the key that was pressed
        if(key==39){//when right arrow key is pressed
          user.moveRight=true; 
        }
        if (key==37){
          user.moveLeft=true; //when left arrow key is pressed
        }
        if (key==32){
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
        time += animationDelay;
        Thread.sleep(Math.max(0,time - 
          System.currentTimeMillis()));
      }catch (InterruptedException e) {
        System.out.println(e);
      }//end catch
    }//end while loop
}//end of run

}//end of class

  
         
  
