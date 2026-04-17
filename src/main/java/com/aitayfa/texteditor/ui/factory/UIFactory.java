/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.ui.factory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author berkaysarmasoglu
 */

// ABSTRACT FACTORY
public interface UIFactory {
    JButton createButton(String text);
    JPanel createPanel();
    JTextArea createTextArea();
}
