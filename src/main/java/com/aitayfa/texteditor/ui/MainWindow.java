/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.ui;
import com.aitayfa.texteditor.config.EditorSettings;
import com.aitayfa.texteditor.ui.factory.*;
import com.aitayfa.texteditor.command.*;
import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author berkaysarmasoglu
 */

// FACTORY PATTERN
public class MainWindow extends JFrame{
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private UIFactory uiFactory;
    private JTextArea textArea;
    
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
        
        mainPanel.add(createEditorScreen(), "EDITOR_SCREEN"); // editör ekranı
        mainPanel.add(createStartScreen(), "START_SCREEN"); // başlangıç/hoş geldin ekranı
        
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
        btnNewFile.addActionListener(e -> {
            textArea.setText("");
            cardLayout.show(mainPanel, "EDITOR_SCREEN");
        });
        
        Command openCommand = new OpenFileCommand(this, textArea, () -> {
            cardLayout.show(mainPanel, "EDITOR_SCREEN"); // İşlem başarılıysa editöre geç
        });
        
        // Butona tıklanınca komutu çalıştır
        btnOpenFile.addActionListener(e -> openCommand.execute());

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
        
        // editörün input kısmını oluşturacak metin kutusu
        textArea = uiFactory.createTextArea();
        
        // Yazıların taşmaması ve scroll (kaydırma) çubuğu çıkması için JScrollPane
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(uiFactory.createBorder());
        
        // MENU BAR
        JMenuBar menuBar = uiFactory.createMenuBar();
        
        // FILE MENU
        JMenu menuFile = uiFactory.createMenu("Dosya");
        JMenuItem itemSave = uiFactory.createMenuItem("Kaydet");
        JMenuItem itemSaveAs = uiFactory.createMenuItem("Farklı Kaydet");
        JMenuItem itemBack = uiFactory.createMenuItem("Giriş Ekranına Dön");
        
        Command saveCommand = new SaveFileCommand(this, textArea);
        Command saveAsCommand = new SaveAsFileCommand(this, textArea);
        itemSave.addActionListener(e -> saveCommand.execute());
        itemSaveAs.addActionListener(e -> saveAsCommand.execute());
        itemBack.addActionListener(e -> cardLayout.show(mainPanel, "START_SCREEN"));
        
        menuFile.add(itemSave);
        menuFile.add(itemSaveAs);
        menuFile.addSeparator();
        menuFile.add(itemBack);
        
        // EDIT MENU
        JMenu menuEdit = uiFactory.createMenu("Düzenle");
        JMenuItem itemFind = uiFactory.createMenuItem("Bul");
        JMenuItem itemReplace = uiFactory.createMenuItem("Değiştir");
        JMenuItem itemClearHighlights = uiFactory.createMenuItem("Arama Vurgularını Temizle");

        Command findCommand = new FindCommand(this, textArea);
        Command replaceCommand = new ReplaceCommand(this, textArea);
        itemFind.addActionListener(e -> findCommand.execute());
        itemReplace.addActionListener(e -> replaceCommand.execute());
        itemClearHighlights.addActionListener(e -> textArea.getHighlighter().removeAllHighlights());
        menuEdit.add(itemFind);
        menuEdit.add(itemReplace);
        menuEdit.add(itemClearHighlights);
        
        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        panel.add(menuBar, BorderLayout.NORTH);
                
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // auto save (default 30 saniyede bir)
        javax.swing.Timer autoSaveTimer = new javax.swing.Timer(settings.getAutoSavePeriod(), e -> {
            String backupPath = EditorSettings.getInstance().getAutoSavePath();
            
            // Sadece metin kutusu boş değilse yedekle
            if (!textArea.getText().trim().isEmpty()) {
                try (FileWriter writer = new FileWriter(backupPath)) {
                    writer.write(textArea.getText());
                    System.out.println("Arka planda otomatik kayıt alındı: " + backupPath);
                } catch (IOException ex) {
                    System.err.println("Otomatik kayıt başarısız: " + ex.getMessage());
                }
            }
        });
        autoSaveTimer.start();

        return panel;
    }
    
}
