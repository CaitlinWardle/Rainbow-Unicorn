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
import java.util.Random;
import javax.swing.JPanel;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
//importing all libraries needed

public class Board  extends JPanel implements Runnable, MouseListener
{
//global variables (must exist everywhere) 
int lives= 3;
int score=0;
boolean ingame = true;
private Dimension d;
int BOARD_WIDTH=1000;
int BOARD_HEIGHT=500;
int winStreak=0;
int x = 0;
double armyspeed; 
BufferedImage unicornpic; //picture of playable character
BufferedImage sadpic;  //enemy sadaces
BufferedImage ball;  //ball fired when player shoots
BufferedImage tear; //enemy tears (bombs) 
BufferedImage RIP; //Picture Displayed If you die
BufferedImage rainbow;
BufferedImage injured; 
private Thread animator;
Player user;// creating users with actions
shot shooting;
tear bomb;
Rainbow RainbowHappy; 
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
        armyspeed=2; 
        user= new Player (BOARD_WIDTH/2,BOARD_HEIGHT-90,5); //screating a user at bottom of board
        shooting=new shot (user.x,BOARD_HEIGHT-5,5); //shot comes from user but start off Screen screen
        int enemyX=10;
        int enemyY=10; //enemy army starting location
        bomb= new tear (range,enemyY,5);
        RainbowHappy= new Rainbow (-100,100,5);
        for (int i=0; i<army.length; i++){
          bomb.dropped=true;
          army[i]= new Enemy (enemyX,enemyY,armyspeed);
          enemyX+=60; //creating row of enemy 60 units apart from each other
          if (i==11){ //new row after 12 enemy sad faces
              enemyX=45; //set new enemy row so it wont overlap
              enemyY+=40; //move second row down
          }
    }
        //set up all needed images
        try{
            injured=ImageIO.read(this.getClass().getResource ("Injured.png"));
        }
        catch (IOException e){
            System.out.println ("Unreadable image");
        }
        try{
            tear=ImageIO.read(this.getClass().getResource ("tears.png"));
        }
        catch (IOException e){
            System.out.println ("Unreadable image");
        }
        try{
            rainbow=ImageIO.read(this.getClass().getResource ("rainbowhappy.png"));
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
if (RainbowHappy.alive==true){
    g.drawImage (rainbow,RainbowHappy.x,RainbowHappy.y,20,20,null);
}
if (bomb.dropped==true){
             bomb.y+=5; //bombfalls
             if (bomb.y>500){ //once off screen
                 bomb.dropped=false;
             }
             if (user.x<=bomb.x && bomb.x<=user.x+50 && user.y==bomb.y){
                 user.injured=true; 
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
        if (army1.x - 1 <= shooting.x && shooting.x <= army1.x + 35 && army1.y - 1 <= shooting.y && shooting.y <= army1.y) {
            if (army1.alive==true){
                score+=10;
                shooting.y=800;
                shooting.x=900;  //set the ball to be invisable (off of visible screen) 
                shooting.fired=false; //reset
                army1.alive = false; //if you hit enemy they die
            }
        }
       if (RainbowHappy.x<=shooting.x && shooting.x<=RainbowHappy.x +30 && RainbowHappy.y<=shooting.y && shooting.y<=RainbowHappy.y){
         if (RainbowHappy.alive==true){ //if you hit the speacle rainbowhappy bonus 
              lives+=1; //gain a life
              RainbowHappy.alive=false; 
         }
       }
    }
}
if (army[15].alive==false || army[11].alive==false){ //if you hit one "speacle" sadfaces
    RainbowHappy.moveRight=true;  //rainbowhappy starts to more right
    
}
if (RainbowHappy.moveRight==true){//move the rainbow happy across the screen
    RainbowHappy.x+=2; 
}
if (RainbowHappy.alive==false){ //if you hit the rainbow happy
    RainbowHappy.moveRight=false; //it stops moving
    RainbowHappy.x=-100;//resets to original position
    RainbowHappy.y=100; 
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
if (user.injured==true){ //if player is hit by bomb
     lives-=1; //take away one life
     g.drawImage(injured, user.x, user.y, 50, 50, null); //draw injured image
     user.injured=false;//turn off injured images (player will flash red) 
}
moveEnemy(); 
    for (Enemy army1 : army) {
        if (army1.alive == true) {
            //only draw enemy if it remains unhit by player
            g.drawImage(sadpic, army1.x, army1.y, 30, 30, null);
        }
    }

if (lives<=0){ //you die
    winStreak=0; 
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
        g.setColor(Color.white);
        g.fillRect(450,150,100,30);
        g.setColor (Color.black);
        g.setFont(small);
        g.drawString ("Replay?",470,170);
        g.setColor(Color.white);
        g.fillRect(450,250,100,30);
        g.setColor (Color.black);
        g.setFont(small);
        g.drawString ("Exit",490,265);
        g.drawString ("Replay?",470,170);
        g.setColor (Color.white);
        g.setFont (small);
        g.drawString ("Win Streak: "+winStreak,450,130);
    }
}
if (score>=240){ // you win
    for (Enemy army1 : army) {
        //stop army
        army1.moveLeft = false;
        army1.moveRight = false;
        g.setColor (Color.red);
        g.fillRect (0,0,1000,83);
        g.setColor (Color.orange);
        g.fillRect (0,84,1000,83);
        g.setColor (Color.yellow);
        g.fillRect (0,168,1000,83);
        g.setColor (Color.green);
        g.fillRect (0,252,1000,83);
        g.setColor (Color.blue);
        g.fillRect (0,336,1000,83);
        g.setColor (Color.MAGENTA); //create celebration rainbow
        g.fillRect (0,420,1000,83);
        g.setColor(Color.white); //display back screen
        Font big = new Font("Helvetica", Font.BOLD, 35);
        FontMetrics m = this.getFontMetrics(big);
        g.fillRect(300,50,400,300);
        g.setColor(Color.black);
        g.setFont(big);
        g.drawString("YOU  WON!!!", 400, 80); //winning banner
        g.setColor(Color.black);
        g.fillRect(450,150,100,30);
        g.setColor (Color.white);
        g.setFont(small);
        g.drawString ("Replay?",470,170);
        g.setColor(Color.black);
        g.fillRect(450,250,100,30);
        g.setColor (Color.white);
        g.setFont(small);
        g.drawString ("Exit",490,265);//set up buttons
        g.setColor (Color.black);
        g.setFont (small);
        g.drawString ("Win Streak: "+winStreak,450,130);
    }
}
Toolkit.getDefaultToolkit().sync();
g.dispose();
}
public void moveEnemy(){
    for (Enemy army1 : army) {
        if (army1.moveLeft == true) {
            army1.x -= armyspeed; //army shifts left
        }
        if (army1.moveRight == true) {
            army1.x += armyspeed; //army shifts right
        }
    }
    for (Enemy army2 : army) {
        if (army2.x > BOARD_WIDTH) {
            for (Enemy army1 : army) {
                army1.moveLeft = true; //when edge of screen hit change directions
                army1.moveRight = false;
                army1.y += 5; //when army hits edge of screen move down
                armyspeed+=0.0015; //speed up at y component increases
            }
        }
        if (army2.x < 0) {
            for (Enemy army1 : army) {
                army1.moveRight = true; //when edge of screen hit change directions
                army1.moveLeft = false;
                army1.y += 5;
                armyspeed+=0.0015; //speed up at y component increases
            }
        }
        if (army2.y>500){ //if the army reaches the ground you die
            lives=0;
        }
    }
}
public void savescore() throws FileNotFoundException{
     FileOutputStream fos= new FileOutputStream("highscore.txt", true);
     PrintWriter pw= new PrintWriter(fos);
     if (winStreak>=1){         
          pw.println (winStreak);
          pw.close();
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
   int x=e.getX();
   int y=e.getY();
   if (450<=x && x<=550 && 150<= y && y<=180 && (score==240 ||lives==0)){//if the game ends and they click replay
       lives=3; //reset lives
       armyspeed=2; //reset army speed
       if (score==240){ //reset score and keep track of wins
           score=0;   
           winStreak+=1; 
       }
       else{
           score=0;
           winStreak=0;
       }
       int EnemyX=10;
       int EnemyY=10; 
       for (int i=0; i<army.length; i++){ //reset each army member
           army[i].alive=true;//set all of them to be visible again      
           army[i].moveLeft=true; //set them to move left
           army[i].x=EnemyX; 
           army[i].y =EnemyY; //reset first row
           EnemyX+=60; //creating row of enemy 60 units apart from each other
           if (i==11){ //new row after 12 enemy sad faces
              EnemyX=45; //set new enemy row so it wont overlap
              EnemyY+=40; //move second row down
          }
       }
       RainbowHappy.x=-100;//reset the rainbowhappy
       RainbowHappy.y=100;
       RainbowHappy.moveRight=false;
       RainbowHappy.alive=true; 
   }
   if (450<=x && x<=550 && 250<= y && y<=280 && (score==240 || lives==0)){ //if game ends and they click exit
       try {
       savescore();
       } catch (FileNotFoundException ex) {
           Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
       }
       System.exit(1); //exit page
   }
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
