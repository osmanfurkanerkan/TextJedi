/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.ui.factory;

/**
 *
 * @author berkaysarmasoglu
 */

import javax.swing.*;
import java.awt.*;

// CONCRETE FACTORY
public class DarkUIFactory implements UIFactory {
    
    @Override
    public JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(60, 60, 60)); // koyu gri
        button.setForeground(Color.WHITE); // beyaz yazı
        button.setFocusPainted(false);
        return button;
    }

    @Override
    public JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(40, 40, 40)); // Koyu arka plan
        return panel;
    }

    @Override
    public JTextArea createTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setBackground(new Color(43, 43, 43));
        textArea.setForeground(new Color(169, 183, 198)); // Açık gri/mavi yazı (IDE stili)
        textArea.setCaretColor(Color.WHITE); // Beyaz imleç
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        return textArea;
    }
}
