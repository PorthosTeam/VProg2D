package com.mygdx.game.desktop;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class ObjectAssetPanel extends JPanel {

    public String objectName;
    public JPanel parentPanel;

    public ObjectAssetPanel(String name) {
        super();
        setLayout(new BorderLayout(0,0));
        setMaximumSize(new Dimension(200, 29));
        
        JButton nameBtn = new JButton(name);
        nameBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                    "Clicked", "Error", 0);
            }
        });
        
        add(nameBtn, BorderLayout.NORTH);

        LineBorder border = new LineBorder(null);
        setBorder(border);
    }
}
