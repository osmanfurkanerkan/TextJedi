/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.ui.factory;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
/**
 *
 * @author berkaysarmasoglu
 */

// CONCRETE FACTORY
public class LightUIFactory implements UIFactory{
    
    @Override
    public JButton createButton(String text){
        JButton button = new JButton(text);
        button.setBackground(new Color(230,230,230)); // açık gri
        button.setForeground(Color.BLACK); // siyah yazı
        button.setFocusPainted(false);
        return button;
    }
    
    @Override
    public JPanel createPanel(){
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE); // beyaz 
        return panel;
    }
    
    @Override
    public JTextArea createTextArea(){
        JTextArea textArea = new JTextArea();
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(Color.BLACK);
        textArea.setCaretColor(Color.BLACK); // İmleç rengi
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        return textArea;
    }
    
    @Override
    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(240, 240, 240));
        menuBar.setOpaque(true);
        return menuBar;
    }

    @Override
    public JMenu createMenu(String text) {
        JMenu menu = new JMenu(text);
        menu.setForeground(Color.BLACK);
        return menu;
    }

    @Override
    public JMenuItem createMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setBackground(Color.WHITE);
        item.setForeground(Color.BLACK);
        item.setOpaque(true);
        return item;
    }

    @Override
    public Border createBorder() {
        // açık gri kenarlık
        return BorderFactory.createLineBorder(new Color(200, 200, 200), 1);
    }
    
}
