package com.mygdx.game;

import java.io.Serializable;
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
  * @date Mar. 22th 2015
  * @module Node
  */

//JFrame class, actual logic done in here
public class Node extends JPanel implements ActionListener, Serializable{
  private int[] number;             //Test to store value
  
  private GridLayout layout;        //Layout for user interface
  
  private JPanel inputArea;         //User interface
  private JTextField name;          //Greeting
  private JTextField[] insertField; //User input field
  private JButton[] removeButton;   //Button per input field, removes them
  
  private Node parent;
  private Node[] children;
  
  public Node(){
    //Initial Constructor statement, set the defaults
    number = new int[1]; 
    insertField = new JTextField[1];
    removeButton = new JButton[1];
    number[0] = 0;
    parent = null;
    children = null;
    
    //Set up the overarching layout to simplify GUI construction
    layout = new GridLayout(2, 1);
    setLayout(layout);
    
    //Set up user interactibles
    inputArea = new JPanel();
    layout = new GridLayout(0, 3);
    inputArea.setLayout(layout);
    
    //Build components and add them
    name = new JTextField(10);
    insertField[0] = new JTextField(10);
    inputArea.add(insertField[0]);
    removeButton[0] = new JButton("X");
    inputArea.add(removeButton[0]);
    add(name);
    add(inputArea);
    
    //Set them up with actionlisteners
    name.addActionListener(this);
    insertField[0].addActionListener(this);
    removeButton[0].addActionListener(this);
  }
  
  public void actionPerformed(ActionEvent event){
  //Handle all event behaviors
    for(int i = 0; i < insertField.length; ++i){
      if(event.getSource() == insertField[i]){
      //If change to textfield, make it an int, store it
        number[i] = Integer.parseInt(insertField[i].getText());
      }
      else if(event.getSource() == removeButton[i]){
      //If click removeButton, remove that row of text and button
        removeValue(i);
      }
    }
  }
  
  private void addValue(){
    
  }
  
  private void removeValue(int i){
    //Clear values
    inputArea.remove(insertField[i]);
    inputArea.remove(removeButton[i]);
    
    JTextField[] insertTemp = new JTextField[insertField.length - 1];
    JButton[] removeTemp = new JButton[insertField.length - 1];
    int[] numberTemp = new int[insertField.length - 1];
    
    //Resize the array
    for(int j = 0; j < i; ++j){
    //Copy the values up to the removed fields
      insertTemp[j] = insertField[j];
      removeTemp[j] = removeButton[j];
      numberTemp[j] = number[j];
    }
    
    for(int j = i + 1; j < insertField.length; ++j){
    //Then move the values up 1 index
      insertTemp[j - 1] = insertField[j];
      removeTemp[j - 1] = removeButton[j];
      numberTemp[j - 1] = number[j];
    }
    
    insertField = insertTemp;
    removeButton = removeTemp;
    number = numberTemp;
  }
  
  private void changeParent(Node p){
  
  }
  
  private void addChild(Node c){
  
  }
  
  private void removeChild(int i){
  
  }
}