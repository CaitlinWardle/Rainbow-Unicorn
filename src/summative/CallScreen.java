/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package summative;
   
import javax.swing.JFrame;

public class CallScreen extends JFrame {

    public CallScreen()
    {
        add(new Board());
        setTitle("Rainbow Unicorn Battle");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000,500);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    public static void main(String[] args) {
        new CallScreen();
    }
}