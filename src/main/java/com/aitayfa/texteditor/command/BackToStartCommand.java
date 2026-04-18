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

public class BackToStartCommand implements Command {
    private MainWindow parent;
    private JTextArea textArea;
    private Runnable onNavigateBack;

    public BackToStartCommand(MainWindow parent, JTextArea textArea, Runnable onNavigateBack) {
        this.parent = parent;
        this.textArea = textArea;
        this.onNavigateBack = onNavigateBack;
    }

    @Override
    public void execute() {
        EditorSettings settings = EditorSettings.getInstance();

        // 1. ADIM: EĞER DEĞİŞİKLİK VARSA DURDUR VE SOR
        if (settings.isModified()) {
            int option = JOptionPane.showConfirmDialog(parent, 
                "Değişiklikler kaydedilmedi. Çıkmadan önce kaydetmek ister misiniz?", 
                "Uyarı", 
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (option == JOptionPane.YES_OPTION) {
                new SaveFileCommand(parent, textArea).execute();
            } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                return; // Buradan aşağısına geçmez, yani ana ekrana DÖNMEZ
            }
        }

        // 2. ADIM: TEMİZLİK (Ana ekrana dönerken arkada çöp bırakma)
        settings.setModified(false);
        settings.setCurrentFilePath(null);
        textArea.setText("");
        parent.updateTitle();

        // 3. ADIM: GEÇİŞİ YAP
        onNavigateBack.run();
    }
}