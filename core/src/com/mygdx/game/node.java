import java.awt.event.*;
import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

/**
  * @description Handle the construction of Node panels, their interior 
  * variables and connections to other nodes.
  * 
  * @author Erik Godreau
  * @date Mar. 27th 2015
  * @filename Node.java
  */

//JFrame class, actual logic done in here
public class Node extends JPanel implements ActionListener, Serializeable{
  private int number;             //Test to store value
  
  private GridLayout layoutIn;    //Layout for user interface
  
  private JPanel inputArea;       //User interface
  private JTextField name;        //Greeting
  private JTextField insertField; //User input field
  
  public GuessingGameFrame(){
    layoutOut = new GridLayout(2, 1);
    setLayout(layoutOut);
    
    greetingLabel = new JLabel();
    add(greetingLabel);
    
    //Set up user interactibles
    inputArea = new JPanel();
    layoutIn = new GridLayout(0, 3);
    inputArea.setLayout(layoutIn);
    
    //Build components
    name = new JTextField(10);
    insertField = new JTextField(10);
    inputArea.add(insertField);
    
    add(name);
    add(inputArea);
    
    //Set them up with actionlisteners
    name.addActionListener(this);
    insertField.addActionListener(this);
  }
  
  public void actionPerformed(ActionEvent event){
    if(event.getSource() == insertField){
      //Read from textfield, make it an int, store it
      number = Integer.parseInt(insertField.getText());
    }
  }
}