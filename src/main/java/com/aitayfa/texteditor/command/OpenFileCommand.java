/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.command;
import com.aitayfa.texteditor.config.EditorSettings;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author berkaysarmasoglu
 */
public class OpenFileCommand implements Command {
    private JFrame parentWindow;
    private JTextArea targetTextArea;
    private Runnable onSuccess; // Başarılı olursa ekranı değiştirmek için bir tetikleyici
    
    // Komut çalışmak için neye ihtiyaç duyuyorsa (pencere, metin kutusu) Constructor'dan alıyoruz.
    public OpenFileCommand(JFrame parentWindow, JTextArea targetTextArea, Runnable onSuccess) {
        this.parentWindow = parentWindow;
        this.targetTextArea = targetTextArea;
        this.onSuccess = onSuccess;
    }
    
    @Override
    public void execute() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(parentWindow);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                targetTextArea.setText(""); // Önceki metni temizle
                String line;
                while ((line = reader.readLine()) != null) {
                    targetTextArea.append(line + "\n"); // Dosyadaki metni ekrana yaz
                }
                
                // İşlem başarılıysa ekranı (Start -> Editor) değiştir
                if (onSuccess != null) {
                    EditorSettings.getInstance().setCurrentFilePath(selectedFile.getAbsolutePath());
                    onSuccess.run();
                }
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parentWindow, "Dosya okunamadı: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
