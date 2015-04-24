package com.mygdx.game.desktop;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Component;

public class ObjectAssetPanel extends JPanel {
    public String objectName;
    public JPanel parentPanel;
    
    public ObjectAssetPanel(String name)
    {
        super();
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setMaximumSize(new Dimension(200, 50));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
        
        JTextField nameText = new JTextField(name);
        nameText.setEditable(false);
        
        JLabel metadataLabel = new JLabel("Loading...");
        
        infoPanel.add(nameText);
        infoPanel.add(metadataLabel);
        
        add(infoPanel);
        
        LineBorder border = new LineBorder(null);
        setBorder(border);
        
        setAlignmentX(Component.LEFT_ALIGNMENT);
    }
    
}
