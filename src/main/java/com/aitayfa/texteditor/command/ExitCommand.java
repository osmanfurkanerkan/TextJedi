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
    private Runnable onExit;
    private Runnable onCancel;

    public ExitCommand(MainWindow parent, JTextArea textArea, Runnable onExit, Runnable onCancel) {
        this.parent = parent;
        this.textArea = textArea;
        this.onExit = onExit;
        this.onCancel = onCancel;
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
                new SaveFileCommand(parent, textArea).execute();
                onExit.run();
            } else if (option == JOptionPane.NO_OPTION) {
                onExit.run();
            } else {
                // CANCEL veya pencere kapatma durumu
                if (onCancel != null) onCancel.run();
            }
        } else {
            onExit.run();
        }
    }
}