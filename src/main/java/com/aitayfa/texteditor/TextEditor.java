/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.aitayfa.texteditor;
import com.aitayfa.texteditor.config.EditorSettings;
import com.aitayfa.texteditor.ui.MainWindow;
import java.awt.*;
import javax.swing.SwingUtilities;

/**
 *
 * @author berkaysarmasoglu
 */

public class TextEditor {
    public static void main(String[] args) {
        EditorSettings settings = EditorSettings.getInstance();
        settings.setTheme("Dark Theme");
        
        // Swing arayüzlerini güvenli bir şekilde (Thread-Safe) başlatmak için kullanılması zorunludur.
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}