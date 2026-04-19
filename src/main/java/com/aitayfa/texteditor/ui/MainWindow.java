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
import javax.swing.undo.UndoManager;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.io.File;

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
    private String lastScreen = "START_SCREEN";
    
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
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null); // ekranın ortası
        
        // card layout ayarları
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        mainPanel.add(createEditorScreen(), "EDITOR_SCREEN"); // editör ekranı
        mainPanel.add(createStartScreen(), "START_SCREEN"); // başlangıç/hoş geldin ekranı
        mainPanel.add(createSettingsScreen(), "SETTINGS_SCREEN"); // ayarlar ekranı
        
        add(mainPanel);
        cardLayout.show(mainPanel, "START_SCREEN"); // çalıştırınca ilk bunu göster
        
        // Uygulamadan çıkarken sor
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                new ExitCommand(MainWindow.this, textArea).execute();
            }
        });
    }
    
    // GİRİŞ EKRANI
    //##########################################
    private JPanel createStartScreen(){
        JPanel panel = uiFactory.createPanel();
        panel.setLayout(new GridBagLayout()); // Butonları tam ortaya hizalamak için

        JButton btnNewFile = uiFactory.createButton("Yeni Dosya Oluştur");
        JButton btnOpenFile = uiFactory.createButton("Mevcut Dosyayı Aç");
        JButton btnSettings = uiFactory.createButton("Ayarlar");

        // Geçici Event Listener: Yeni Dosyaya tıklayınca Editör ekranına geçiş yap
        btnNewFile.addActionListener(e -> {
            // Singleton ayarlarını sıfırla
            EditorSettings settings = EditorSettings.getInstance();
            settings.setCurrentFilePath(null);
            settings.setModified(false);

            textArea.setText(""); 
            updateTitle();
            cardLayout.show(mainPanel, "EDITOR_SCREEN");
        });
        
        Command openCommand = new OpenFileCommand(this, textArea, () -> {
            cardLayout.show(mainPanel, "EDITOR_SCREEN"); // İşlem başarılıysa editöre geç
        });
        
        // Butona tıklanınca komutu çalıştır
        btnOpenFile.addActionListener(e -> openCommand.execute());
        
        btnSettings.addActionListener(e -> {
            lastScreen = "START_SCREEN";
            cardLayout.show(mainPanel, "SETTINGS_SCREEN");
        });

        // Butonları alt alta dizmek için küçük bir alt panel
        JPanel buttonBox = uiFactory.createPanel();
        buttonBox.setLayout(new GridLayout(3, 1, 10, 10)); // 2 satır, 1 sütun, 10px boşluk
        buttonBox.add(btnNewFile);
        buttonBox.add(btnOpenFile);
        buttonBox.add(btnSettings);

        panel.add(buttonBox);
        return panel;
    }
    
    // EDITOR EKRANI
    //##########################################
    private JPanel createEditorScreen() {
        EditorSettings settings = EditorSettings.getInstance();
        JPanel panel = uiFactory.createPanel();
        panel.setLayout(new BorderLayout()); // sağ-sol-üst-alt ve merkez yerleşimi

        // kenarlardan pay
        int p = settings.getPadding();
        panel.setBorder(BorderFactory.createEmptyBorder(p,p,p,p));
        
        // editörün input kısmını oluşturacak metin kutusu
        textArea = uiFactory.createTextArea();
        UndoManager undoManager = new UndoManager();
        textArea.getDocument().addUndoableEditListener(undoManager);
        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void trigger() {
                if (!EditorSettings.getInstance().isModified()) {
                    EditorSettings.getInstance().setModified(true);
                    updateTitle(); // Sadece ilk değişiklikte başlığı güncelle
                }
            }
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { trigger(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { trigger(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { trigger(); }
        });

        // Başlangıçta başlığı ayarla
        updateTitle();
        
        // Yazıların taşmaması ve scroll (kaydırma) çubuğu çıkması için JScrollPane
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(uiFactory.createBorder());
        JComponent lineNumberView = uiFactory.createLineNumberView(textArea);
        lineNumberView.setVisible(settings.isShowLineNumbers());
        scrollPane.setRowHeaderView(lineNumberView);
        scrollPane.getRowHeader().setBackground(lineNumberView.getBackground());
        
        // MENU BAR
        JMenuBar menuBar = uiFactory.createMenuBar();
        
        // FILE MENU
        JMenu menuFile = uiFactory.createMenu("Dosya");
        JMenuItem itemNew = uiFactory.createMenuItem("Yeni");
        JMenuItem itemOpen = uiFactory.createMenuItem("Aç...");
        JMenuItem itemSave = uiFactory.createMenuItem("Kaydet");
        JMenuItem itemSaveAs = uiFactory.createMenuItem("Farklı Kaydet");
        JMenuItem itemBack = uiFactory.createMenuItem("Giriş Ekranına Dön");
        
        Command newCommand = new NewFileCommand(this, textArea);
        Command openCommand = new OpenFileCommand(this, textArea, null); // Callback gerekmiyor
        Command saveCommand = new SaveFileCommand(this, textArea);
        Command saveAsCommand = new SaveAsFileCommand(this, textArea);
        Command backCommand = new BackToStartCommand(this, textArea, () -> {
            cardLayout.show(mainPanel, "START_SCREEN");
        });
        
        // COMMAND + N , CTRL + N
        itemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        itemNew.addActionListener(e -> newCommand.execute());
        // COMMAND + O , CTRL + O
        itemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        itemOpen.addActionListener(e -> openCommand.execute());
        // COMMAND + S , CTRL + S
        itemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        itemSave.addActionListener(e -> saveCommand.execute());
        // COMMAND + SHIFT + S , CTRL + SHIFT + S
        itemSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | InputEvent.SHIFT_DOWN_MASK));
        itemSaveAs.addActionListener(e -> saveAsCommand.execute());
        itemBack.addActionListener(e -> backCommand.execute());
        
        menuFile.add(itemNew);
        menuFile.add(itemOpen);
        menuFile.addSeparator();
        menuFile.add(itemSave);
        menuFile.add(itemSaveAs);
        menuFile.addSeparator();
        menuFile.add(itemBack);
        
        // EDIT MENU
        JMenu menuEdit = uiFactory.createMenu("Düzenle");
        JMenuItem itemUndo = uiFactory.createMenuItem("Geri Al");
        JMenuItem itemRedo = uiFactory.createMenuItem("Yinele");
        JMenuItem itemFind = uiFactory.createMenuItem("Bul");
        JMenuItem itemReplace = uiFactory.createMenuItem("Değiştir");
        JMenuItem itemClearHighlights = uiFactory.createMenuItem("Arama Vurgularını Temizle");
        JMenuItem itemAI = uiFactory.createMenuItem("AI Yardımı");

        Command undoCommand = new UndoCommand(undoManager);
        Command redoCommand = new RedoCommand(undoManager);
        Command findCommand = new FindCommand(this, textArea);
        Command replaceCommand = new ReplaceCommand(this, textArea, undoManager);
        Command aiCommand = new AICommand(this, textArea, undoManager);
        
        // COMMAND + Z , CTRL + Z
        itemUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        itemUndo.addActionListener(e -> undoCommand.execute());
        // COMMAND + Y , CTRL + Y
        itemRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        itemRedo.addActionListener(e -> redoCommand.execute());
        // COMMAND + F , CTRL + F
        itemFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        itemFind.addActionListener(e -> findCommand.execute());
        // COMMAND + H , CTRL + H
        itemReplace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        itemReplace.addActionListener(e -> replaceCommand.execute());
        // COMMAND + E, CTRL + E
        itemClearHighlights.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        itemClearHighlights.addActionListener(e -> textArea.getHighlighter().removeAllHighlights());
        itemAI.addActionListener(e -> aiCommand.execute());
        menuEdit.add(itemUndo);
        menuEdit.add(itemRedo);
        menuEdit.add(itemFind);
        menuEdit.add(itemReplace);
        menuEdit.add(itemClearHighlights);
        menuEdit.addSeparator();
        menuEdit.add(itemAI);
        menuEdit.addMenuListener(new javax.swing.event.MenuListener() {
            @Override
            public void menuSelected(javax.swing.event.MenuEvent e) {
                itemUndo.setEnabled(undoManager.canUndo());
                itemRedo.setEnabled(undoManager.canRedo());
            }
            @Override public void menuDeselected(javax.swing.event.MenuEvent e) {}
            @Override public void menuCanceled(javax.swing.event.MenuEvent e) {}
        });
        
        // VIEW MENU
        JMenu menuView = uiFactory.createMenu("Görüntüle");
        
        JMenu menuTheme = uiFactory.createMenu("Tema");
        JMenuItem itemLightTheme = uiFactory.createMenuItem("Aydınlık Tema");
        itemLightTheme.addActionListener(e -> { applyThemeDynamically("Light Theme"); });
        JMenuItem itemDarkTheme = uiFactory.createMenuItem("Karanlık Tema");
        itemDarkTheme.addActionListener(e -> { applyThemeDynamically("Dark Theme"); });
        menuTheme.add(itemLightTheme);
        menuTheme.add(itemDarkTheme);
        
        JMenu menuFontSize = uiFactory.createMenu("Yazı Boyutu");
        int[] sizes = {12, 14, 16, 18, 20, 24, 28, 36};
        for (int size : sizes) {
            JMenuItem sizeItem = uiFactory.createMenuItem(size + " pt");
            sizeItem.addActionListener(e -> {
                settings.setFontSize(size);
                settings.saveToProperties();
                Font currentFont = textArea.getFont();
                textArea.setFont(new Font(currentFont.getName(), currentFont.getStyle(), size));
            });
            menuFontSize.add(sizeItem);
        }
        
        JMenuItem itemHighlighterColor = uiFactory.createMenuItem("Arama Vurgu Rengini Seç...");
        itemHighlighterColor.addActionListener(e -> {
            Color initialColor = settings.getHighlighterColor();
            Color newColor = JColorChooser.showDialog(this, "Vurgu Rengini Seç", initialColor);
            
            if (newColor != null) { // Kullanıcı iptale basmadıysa
                settings.setHighlighterColor(newColor);
                 settings.saveToProperties();
                textArea.getHighlighter().removeAllHighlights(); 
            }
        });
        
        menuView.add(menuTheme);
        menuView.addSeparator();
        menuView.add(menuFontSize);
        menuView.addSeparator();
        menuView.add(itemHighlighterColor);
        
        // SETTINGS menü
        JMenu menuSettings = uiFactory.createMenu("Ayarlar...");
        
        // JMenu'ler ActionListener yerine MouseListener ile tıklanmayı daha iyi algılar
        menuSettings.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                lastScreen = "EDITOR_SCREEN";
                cardLayout.show(mainPanel, "SETTINGS_SCREEN");
            }
        });
        
        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        menuBar.add(menuView);
        
        menuBar.add(menuSettings);
        panel.add(menuBar, BorderLayout.NORTH);
                
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // auto save (default 30 saniyede bir)
        javax.swing.Timer autoSaveTimer = new javax.swing.Timer(settings.getAutoSavePeriod() * 1000, e -> {
            String backupPath = EditorSettings.getInstance().getAutoSavePath();
            
            // Sadece metin kutusu boş değilse yedekle
            if (!textArea.getText().trim().isEmpty()) {
                try (FileWriter writer = new FileWriter(backupPath  + File.separator + "__texteditor_autosave.txt")) {
                    writer.write(textArea.getText());
                    System.out.println("Arka planda otomatik kayıt alındı: " + backupPath + File.separator + "__texteditor_autosave.txt");
                } catch (IOException ex) {
                    System.err.println("Otomatik kayıt başarısız: " + ex.getMessage());
                }
            }
        });
        autoSaveTimer.start();

        return panel;
    }
    
     // AYARLAR EKRANI
    //##########################################
    private JPanel createSettingsScreen() {
        JPanel panel = uiFactory.createPanel();
        panel.setLayout(new GridBagLayout()); // Daha düzenli bir form yapısı için
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        EditorSettings settings = EditorSettings.getInstance();

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblTitle = uiFactory.createLabel("Uygulama Ayarları");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(lblTitle, gbc);

        // Word Wrap
        gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(uiFactory.createLabel("Sözcük Kaydırma:"), gbc);
        gbc.gridx = 1;
        JCheckBox chkWrap = uiFactory.createCheckBox("Aktif", settings.isWordWrapEnabled());
        panel.add(chkWrap, gbc);
        
        // Line Numbers
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(uiFactory.createLabel("Satır Numaraları:"), gbc);
        gbc.gridx = 1;
        JCheckBox chkLineNumbers = uiFactory.createCheckBox("Göster", settings.isShowLineNumbers());
        panel.add(chkLineNumbers, gbc);

        // Padding
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(uiFactory.createLabel("Kenar Boşluğu (px):"), gbc);
        gbc.gridx = 1;
        JSpinner spinPadding = uiFactory.createSpinner(settings.getPadding(), 0, 50, 1);
        panel.add(spinPadding, gbc);

        // Auto Save Period
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(uiFactory.createLabel("Oto Kayıt Sıklığı (sn):"), gbc);
        gbc.gridx = 1;
        JSpinner spinAutoSave = uiFactory.createSpinner(settings.getAutoSavePeriod(), 10, 600, 10);
        panel.add(spinAutoSave, gbc);

        // Auto Save Path
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(uiFactory.createLabel("Oto Kayıt Klasörü:"), gbc);

        gbc.gridx = 1;
        // İçine metin kutusu ve butonu yan yana koyacağımız alt panel
        JPanel pathPanel = uiFactory.createPanel(); 
        pathPanel.setLayout(new BorderLayout(5, 0));

        // Yolu gösterecek metin kutusu
        String currentPath = settings.getAutoSavePath() != null ? settings.getAutoSavePath() : "";
        JTextField txtAutoSavePath = uiFactory.createTextField(currentPath);
        txtAutoSavePath.setEditable(false); // Elle yazıp bozmasın, sadece butonla seçsin
        pathPanel.add(txtAutoSavePath, BorderLayout.CENTER);

        // Klasör seçme butonu
        JButton btnSelectPath = uiFactory.createButton("Seç...");
        btnSelectPath.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Otomatik Kayıt Klasörünü Seçin");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Sadece klasör seçtirtiyoruz

            int option = chooser.showOpenDialog(mainPanel);
            if (option == JFileChooser.APPROVE_OPTION) {
                txtAutoSavePath.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        pathPanel.add(btnSelectPath, BorderLayout.EAST);

        panel.add(pathPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        JButton btnBack = uiFactory.createButton("Geri Dön");
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, lastScreen));
        panel.add(btnBack, gbc);
        
        gbc.gridx = 1;
        JButton btnSave = uiFactory.createButton("Uygula");
        btnSave.addActionListener(e -> {
            settings.setWordWrapEnabled(chkWrap.isSelected());
            settings.setShowLineNumbers(chkLineNumbers.isSelected());
            settings.setPadding((int) spinPadding.getValue());
            settings.setAutoSavePeriod((int) spinAutoSave.getValue());
            settings.setAutoSavePath(txtAutoSavePath.getText());
            settings.saveToProperties();
            applyThemeDynamically(settings.getTheme());
        });
        panel.add(btnSave, gbc);

        
        
        return panel;
    }
    
    // başlığı güncelle
    public void updateTitle() {
        EditorSettings settings = EditorSettings.getInstance();
        String title = "TextEditor - " + settings.getCurrentFileName();

        if (settings.isModified()) {
            title += " *"; // Değişiklik varsa yıldız ekle
        }

        setTitle(title);
    }
    
    // temayı anında değiştir
    private void applyThemeDynamically(String newTheme) {
        EditorSettings settings = EditorSettings.getInstance();
        settings.setTheme(newTheme);
        settings.saveToProperties();

        if (newTheme.equals("Light Theme")) {
            uiFactory = new LightUIFactory();
        } else if(newTheme.equals("Dark Theme")) {
            uiFactory = new DarkUIFactory();
        }

        // mevcut durumu yedekle
        String currentText = textArea.getText();
        boolean wasModified = settings.isModified();

        mainPanel.removeAll();

        // baştan inşa et
        JPanel startScreen = createStartScreen();
        JPanel editorPanel = createEditorScreen();

        // yedekleri geri yükle
        textArea.setText(currentText);
        settings.setModified(wasModified);
        updateTitle();

        // ekranları yerleştir ve göster
        mainPanel.add(startScreen, "START_SCREEN");
        mainPanel.add(editorPanel, "EDITOR_SCREEN");
        mainPanel.add(createSettingsScreen(), "SETTINGS_SCREEN");
        cardLayout.show(mainPanel, "EDITOR_SCREEN");

        // İŞLETİM SİSTEMİNİ EKRANI ZORLA YENİLEMEYE İT (Görsel bug kalmasın)
        this.revalidate();
        this.repaint();
    }
    
}
