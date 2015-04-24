package com.mygdx.game.desktop;

import com.mygdx.game.WindowBoundsChecker;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;

public class AboutFrame extends JFrame
{
    public AboutFrame()
    {
        super("About VProg2D");
        
        Rectangle bounds = WindowBoundsChecker.getCenteredBounds(220, 300);
        setBounds(bounds);
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        EmptyBorder margin = new EmptyBorder(10, 10, 10, 10);
        
        ImageIcon logoIcon = new ImageIcon("logo_small.png");
        JLabel logo = new JLabel(logoIcon);
        logo.setBorder(margin);
        
        String text = "<html>"
            + "VProg2D is a visual game editor.<br/>"
            + " It allows for flexibly creating simple games.<br/>"
            + " Also this text needs improving!</html>";
        JLabel label = new JLabel(text);
        label.setBorder(margin);
        // Need this to ensure proper word-wrapping.
        label.setSize(new Dimension(bounds.width, bounds.height));
        
        add(logo, BorderLayout.NORTH);
        add(label, BorderLayout.CENTER);
        setVisible(true);
    }
}
