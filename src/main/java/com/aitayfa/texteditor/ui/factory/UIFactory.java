/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.ui.factory;
import javax.swing.*;
import javax.swing.border.Border;

/**
 *
 * @author berkaysarmasoglu
 */

// ABSTRACT FACTORY
public interface UIFactory {
    JButton createButton(String text);
    JPanel createPanel();
    JTextArea createTextArea();
    JMenuBar createMenuBar();
    JMenu createMenu(String text);
    JMenuItem createMenuItem(String text);
    Border createBorder(); // Kenarlık rengi için
}
