/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.command;
import com.aitayfa.texteditor.config.EditorSettings;
import com.aitayfa.texteditor.ui.MainWindow;
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
    private MainWindow parent;
    private JTextArea textArea;
    private Runnable onSuccess;

    public OpenFileCommand(MainWindow parent, JTextArea textArea, Runnable onSuccess) {
        this.parent = parent;
        this.textArea = textArea;
        this.onSuccess = onSuccess;
    }

    @Override
    public void execute() {
        EditorSettings settings = EditorSettings.getInstance();

        // 1. KAYDEDİLMEMİŞ DEĞİŞİKLİK KONTROLÜ
        if (settings.isModified()) {
            int option = JOptionPane.showConfirmDialog(parent, 
                "Mevcut dosyada kaydedilmemiş değişiklikler var. Yeni bir dosya açmadan önce kaydetmek ister misiniz?", 
                "Dosya Aç", 
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (option == JOptionPane.YES_OPTION) {
                // Kaydetmeyi seçtiyse önce kaydet komutunu çalıştır
                new SaveFileCommand(parent, textArea).execute();
            } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                // İptale bastıysa veya pencereyi kapattıysa açma işlemini durdur
                return; 
            }
            // NO_OPTION (Hayır) seçilirse hiçbir şey yapmadan aşağıdaki kodlardan (dosya açma) devam eder.
        }

        // 2. DOSYA SEÇİCİ (Dosya açma işlemi başlıyor)
        JFileChooser chooser = new JFileChooser();
        
        int chooserOption = chooser.showOpenDialog(parent);
        if (chooserOption == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            
            try {
                String content = new String(java.nio.file.Files.readAllBytes(selectedFile.toPath()));
                textArea.setText(content);
                
                // Singleton ayarlarını yeni açılan dosyaya göre güncelle
                settings.setCurrentFilePath(selectedFile.getAbsolutePath());
                
                // setText() komutu dinleyiciyi anlık tetikleyip * koyduracağı için, 
                // işlemi hemen ardından zorla false yapıp başlığı temizliyoruz.
                settings.setModified(false); 
                parent.updateTitle();
                
                // Başarı durumunda geri arama (callback) fonksiyonunu çalıştır
                if (onSuccess != null) {
                    onSuccess.run();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, 
                    "Dosya okunamadı: " + ex.getMessage(), 
                    "Hata", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        }
    }
}
