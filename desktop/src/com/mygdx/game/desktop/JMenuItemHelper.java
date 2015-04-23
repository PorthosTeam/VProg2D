package com.mygdx.game.desktop;

import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;

public final class JMenuItemHelper
{
    private JMenuItemHelper(){}
    
    public static JMenuItem newJMenuItem(String name, String iconFilename)
    {
        ImageIcon icon = new ImageIcon(iconFilename);
        JMenuItem item = new JMenuItem(name, icon);
        return item;
    }
    
    public static JMenuItem newJMenuItem(String name, ActionListener action)
    {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(action);
        return item;
    }
    
    public static JMenuItem newJMenuItem
    (String name, String iconFilename, ActionListener action)
    {
        ImageIcon icon = new ImageIcon(iconFilename);
        JMenuItem item = new JMenuItem(name, icon);
        item.addActionListener(action);
        return item;
    }
}
