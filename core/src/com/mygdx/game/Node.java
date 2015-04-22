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

  private class Row{
  //Nested class to hold the values per a Row
    public int value;
    public Node attachedNode;
    public JTextField textField;
    public JButton removeButton;
  }

  public int x, y;                  //Coordinates in the environment

  private Row[] rows;
  
  private GridLayout layout;        //Layout for user interface
  
  private JPanel inputArea;         //User interface
  private JTextField name;          //Greeting
  
  public Node(){
    //Initial Constructor statement, set the defaults
    x = 50;
    y = 50;
    
    rows = new Row[1];
     
    rows[0].value = 0;
    rows[0].attachedNode = null;
    
    //Set up the overarching layout to simplify GUI construction
    layout = new GridLayout(2, 1);
    setLayout(layout);
    
    //Set up user interactibles
    inputArea = new JPanel();
    layout = new GridLayout(0, 3);
    inputArea.setLayout(layout);
    
    //Build components and add them
    name = new JTextField(10);
    rows[0].textField = new JTextField(10);
    inputArea.add(rows[0].textField);
    rows[0].removeButton = new JButton("X");
    inputArea.add(rows[0].removeButton);
    add(name);
    add(inputArea);
    
    //Set them up with actionlisteners
    name.addActionListener(this);
    rows[0].textField.addActionListener(this);
    rows[0].removeButton.addActionListener(this);
  }
  
  public void actionPerformed(ActionEvent event){
  //Handle all event behaviors
    for(int i = 0; i < rows.length; ++i){
      if(event.getSource() == rows[i].textField){
      //If change to textfield, make it an int, store it
        rows[i].value = Integer.parseInt(rows[i].textField.getText());
      }
      else if(event.getSource() == rows[i].removeButton){
      //If click removeButton, remove that row of text and button
        removeValue(i);
      }
    }
  }
  
  public void addChild(Node in, int i){
    removeChild(i);
    rows[i].attachedNode = in;
  }
  
  public void removeChild(int i){
    rows[i].attachedNode = null;
  }
  
  private void addValue(){
    Row[] rowsTemp = new Row[rows.length + 1];
    
    //Resize the array
    for(int i = 0; i < rows.length; ++i){
    //Copy vals of rows into a temp
      rowsTemp[i] = rows[i];
    }
    
    //Set defaults at the end
    int newSize = rows.length + 1;
    rowsTemp[newSize].value = 0;
    rowsTemp[newSize].attachedNode = null;
    rowsTemp[newSize].textField = new JTextField(10);
    rowsTemp[newSize].removeButton = new JButton("X"); 
    rowsTemp[newSize].textField.addActionListener(this);
    rowsTemp[newSize].removeButton.addActionListener(this);
    
    rows = rowsTemp;
  }
  
  private void removeValue(int i){
    //Clear values
    inputArea.remove(rows[i].textField);
    inputArea.remove(rows[i].removeButton);
    
    Row[] rowsTemp = new Row[rows.length - 1];
    
    //Resize the array
    for(int j = 0; j < i; ++j){
    //Copy the values up to the removed fields
      rowsTemp[j] = rows[j];
    }
    
    for(int j = i + 1; j < rows.length; ++j){
    //Then move the values up 1 index
      rowsTemp[j - 1] = rows[j];
    }
    
    //Set the stored to the temps
    rows = rowsTemp;
  }
}
