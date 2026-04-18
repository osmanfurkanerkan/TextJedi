/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.command;
import com.aitayfa.texteditor.config.EditorSettings;
import com.aitayfa.texteditor.ui.MainWindow;
import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author berkaysarmasoglu
 */
public class SaveAsFileCommand implements Command {
    private JFrame parentWindow;
    private JTextArea targetTextArea;
    
    public SaveAsFileCommand(JFrame parentWindow, JTextArea targetTextArea){
        this.parentWindow = parentWindow;
        this.targetTextArea = targetTextArea;
    }
    
    @Override
    public void execute() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(parentWindow);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            saveToFile(selectedFile);
        }
    }

    // Ortak kaydetme metodu
    public void saveToFile(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(targetTextArea.getText());
            // Başarılı kayıttan sonra dosya yolunu Singleton'a kaydet!
            EditorSettings settings = EditorSettings.getInstance();
            settings.setCurrentFilePath(file.getAbsolutePath());
            settings.setModified(false);
            ((MainWindow)parentWindow).updateTitle(); // başlığı temizle
            JOptionPane.showMessageDialog(parentWindow, "Dosya kaydedildi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentWindow, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
