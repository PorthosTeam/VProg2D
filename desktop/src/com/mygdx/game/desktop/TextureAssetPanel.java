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

import com.mygdx.game.Callable;

public class TextureAssetPanel extends JPanel implements Callable
{
    private JLabel metadataLabel;
    private String metadataString;
    public JPanel parentPanel;
    
    public TextureAssetPanel(String filename)
    {
        super();
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setMaximumSize(new Dimension(200, 50));
        
        ImageIcon icon = new ImageIcon(filename);
        metadataString = icon.getIconWidth() + "x" + icon.getIconHeight();
        Image image = icon.getImage();
        icon.setImage(image.getScaledInstance(50, 50, Image.SCALE_DEFAULT));
        
        JLabel iconContainer = new JLabel();
        iconContainer.setIcon(icon);
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
        
        JTextField nameText = new JTextField(filename);
        nameText.setEditable(false);
        
        metadataLabel = new JLabel("Loading...");
        
        infoPanel.add(nameText);
        infoPanel.add(metadataLabel);
        
        add(iconContainer);
        add(infoPanel);
        
        LineBorder border = new LineBorder(null);
        setBorder(border);
        
        setAlignmentX(Component.LEFT_ALIGNMENT);
    }
    
    @Override
    public void call()
    {
        metadataLabel.setText(metadataString);
        parentPanel.updateUI();
    }
}
