/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.command;
import com.aitayfa.texteditor.config.EditorSettings;
import com.aitayfa.texteditor.ui.MainWindow;
import javax.swing.*;

/**
 *
 * @author berkaysarmasoglu
 */
public class ExitCommand implements Command {
    private MainWindow parent;
    private JTextArea textArea;

    public ExitCommand(MainWindow parent, JTextArea textArea) {
        this.parent = parent;
        this.textArea = textArea;
    }

    @Override
    public void execute() {
        EditorSettings settings = EditorSettings.getInstance();

        if (settings.isModified()) {
            int option = JOptionPane.showConfirmDialog(parent, 
                "Değişiklikler kaydedilmedi. Çıkmadan önce kaydetmek ister misiniz?", 
                "Uygulamadan Çık", 
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (option == JOptionPane.YES_OPTION) {
                // Kaydetmeyi dene, başarılı olursa sistemi kapat
                new SaveFileCommand(parent, textArea).execute();
                System.exit(0);
            } else if (option == JOptionPane.NO_OPTION) {
                // Kaydetmeden doğrudan çık
                System.exit(0);
            }
            // CANCEL veya pencereyi kapatma (X) durumunda hiçbir şey yapma, programda kal
        } else {
            // Değişiklik yoksa sistemi doğrudan kapat
            System.exit(0);
        }
    }
}