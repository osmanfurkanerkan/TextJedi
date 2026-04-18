/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.command;

/**
 *
 * @author berkaysarmasoglu
 */
import com.aitayfa.texteditor.config.EditorSettings;
import com.aitayfa.texteditor.ui.MainWindow;
import javax.swing.*;

public class NewFileCommand implements Command {
    private MainWindow parent;
    private JTextArea textArea;

    public NewFileCommand(MainWindow parent, JTextArea textArea) {
        this.parent = parent;
        this.textArea = textArea;
    }

    @Override
    public void execute() {
        EditorSettings settings = EditorSettings.getInstance();
        
        // Kaydedilmemiş değişiklik kontrolü
        if (settings.isModified()) {
            int option = JOptionPane.showConfirmDialog(parent, 
                "Değişiklikler kaydedilmedi. Kaydetmek ister misiniz?", 
                "Yeni Dosya", 
                JOptionPane.YES_NO_CANCEL_OPTION);
            
            if (option == JOptionPane.YES_OPTION) {
                new SaveFileCommand(parent, textArea).execute();
            } else if (option == JOptionPane.CANCEL_OPTION) {
                return; // İşlemi iptal et
            }
        }
        
        // Dosya durumunu sıfırla
        settings.setCurrentFilePath(null);
        settings.setModified(false);
        textArea.setText("");
        parent.updateTitle();
    }
}