/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.ui;
import com.aitayfa.texteditor.config.EditorSettings;
import com.aitayfa.texteditor.ui.factory.*;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author berkaysarmasoglu
 */

public class MainWindow extends JFrame{
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private UIFactory uiFactory;
    
    public MainWindow(){
        EditorSettings settings = EditorSettings.getInstance();
        String currentTheme = settings.getTheme();
        
        if(currentTheme.equals("Light Theme")){
            uiFactory = new LightUIFactory();
        }
        
        else if(currentTheme.equals("Dark Theme")){
            uiFactory = new DarkUIFactory();
        }
        
        else{
            throw new IllegalArgumentException("UNKNOWN THEME ERROR");
        }
        
        // pencerenin genel ayarları
        setTitle("TextEditor");
        setSize(settings.getWindowWidth(), settings.getWindowHeight());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // ekranın ortası
        
        // card layout ayarları
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        mainPanel.add(createStartScreen(), "START_SCREEN"); // başlangıç/hoş geldin ekranı
        mainPanel.add(createEditorScreen(), "EDITOR_SCREEN"); // editör ekranı
        
        add(mainPanel);
        cardLayout.show(mainPanel, "START_SCREEN"); // çalıştırınca ilk bunu göster
    }
    
    // giriş ekranı
    private JPanel createStartScreen(){
        JPanel panel = uiFactory.createPanel();
        panel.setLayout(new GridBagLayout()); // Butonları tam ortaya hizalamak için

        JButton btnNewFile = uiFactory.createButton("Yeni Dosya Oluştur");
        JButton btnOpenFile = uiFactory.createButton("Mevcut Dosyayı Aç");

        // Geçici Event Listener: Yeni Dosyaya tıklayınca Editör ekranına geçiş yap
        btnNewFile.addActionListener(e -> cardLayout.show(mainPanel, "EDITOR_SCREEN"));

        // Butonları alt alta dizmek için küçük bir alt panel
        JPanel buttonBox = uiFactory.createPanel();
        buttonBox.setLayout(new GridLayout(2, 1, 10, 10)); // 2 satır, 1 sütun, 10px boşluk
        buttonBox.add(btnNewFile);
        buttonBox.add(btnOpenFile);

        panel.add(buttonBox);
        return panel;
    }
    
    // editör ekranı
    private JPanel createEditorScreen() {
        EditorSettings settings = EditorSettings.getInstance();
        JPanel panel = uiFactory.createPanel();
        panel.setLayout(new BorderLayout()); // sağ-sol-üst-alt ve merkez yerleşimi

        // kenarlardan pay
        int p = settings.getPadding();
        panel.setBorder(BorderFactory.createEmptyBorder(p,p,p,p));
        
        JButton btnBack = uiFactory.createButton("<- Giriş Ekranına Dön");
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "START_SCREEN"));
        panel.add(btnBack, BorderLayout.NORTH);

        // editörün input kısmını oluşturacak metin kutusu
        JTextArea textArea = uiFactory.createTextArea();
        
        // Yazıların taşmaması ve scroll (kaydırma) çubuğu çıkması için JScrollPane
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null); // kenarlıkları kaldır
        
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
    
}
