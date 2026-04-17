/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.aitayfa.texteditor;
import com.aitayfa.texteditor.config.EditorSettings;
import com.aitayfa.texteditor.ui.factory.DarkUIFactory;
import com.aitayfa.texteditor.ui.factory.LightUIFactory;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author berkaysarmasoglu
 */

public class TextEditor {
    public static void main(String[] args) {
        // Singleton 
        EditorSettings settings = EditorSettings.getInstance();
        
        DarkUIFactory factory = new DarkUIFactory();
        JPanel panel = factory.createPanel();
        panel.setVisible(true);
    }
}