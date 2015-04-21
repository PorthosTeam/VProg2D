package com.mygdx.game.desktop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class JMenuBarBuilder
{
    private JMenuBar jMenuBar;
    
    public JMenuBarBuilder()
    {
        jMenuBar = new JMenuBar();
    }
    public JMenuBarBuilder(JMenuBar jMenuBar)
    {
        this.setJMenuBar(jMenuBar);
    }
    public JMenuBar getJMenuBar()
    {
        return jMenuBar;
    }
    public void setJMenuBar(JMenuBar newJMenuBar)
    {
        jMenuBar = newJMenuBar;
    }
    
    private static JMenuItem newJMenuItemWithIcon
    (String name, String iconFilename)
    {
        ImageIcon icon = new ImageIcon(iconFilename);
        JMenuItem item = new JMenuItem(name, icon);
        return item;
    }
    
    public static JMenu buildFileMenu()
    {
        JMenu menu = new JMenu("File");
        
        // "new_" is icky, but "new" is a reserved keyword.
        JMenuItem new_ = newJMenuItemWithIcon("New", "createProject_small.png");
        new_.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JLabel popupTxt = new JLabel("selfmade");
                JFrame popup = new JFrame();
                popup.add(popupTxt);
                popup.setVisible(true);
                ((JMenuItem )e.getSource()).setText("EDIT!");
            }
        });
        JMenuItem open = newJMenuItemWithIcon("Open", "loadProject_small.png");
        JMenuItem save = newJMenuItemWithIcon("Save", "saveProject_small.png");
        
        menu.add(new_);
        menu.add(open);
        menu.add(save);
        
        return menu;
    }
    
    public static JMenu buildProjectMenu() {
        JMenu menu = new JMenu("Project");
        
        JMenuItem build = newJMenuItemWithIcon("Build", "construct_small.png");
        JMenuItem test = newJMenuItemWithIcon("Test", "test_small.png");
        JMenuItem run = newJMenuItemWithIcon("Run", "runProgram_small.png");
        
        menu.add(build);
        menu.add(test);
        menu.add(run);
        
        return menu;
    }
    
    public static JMenu buildHelpMenu() {
        JMenu menu = new JMenu("Help");
        
        JMenuItem about = new JMenuItem("About");
        
        menu.add(about);
        
        return menu;
    }
}
