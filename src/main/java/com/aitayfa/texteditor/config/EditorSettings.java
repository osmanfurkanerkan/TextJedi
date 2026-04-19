/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.config;
import java.io.File;
import java.awt.Color;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileOutputStream;
/**
 *
 * @author berkaysarmasoglu
 */

// SINGLETON PATTERN
public class EditorSettings {
    // volatile: Değişkenin CPU cache'i yerine doğrudan ana bellekten (RAM) okunmasını garanti eder.
    private static volatile EditorSettings instance;
    
    // Uygulama genelinde tutulacak ayarlar
    private int fontSize;
    private String theme;
    private boolean wordWrapEnabled;
    private int windowWidth;
    private int windowHeight;
    private int padding;
    private String autoSavePath;
    private int autoSavePeriod;
    private String currentFilePath;
    private Color highlighterColor;
    private boolean modified;
    private boolean showLineNumbers;
    
    // Constructor'ı private yapıyoruz ki dışarıdan 'new' anahtar kelimesiyle başka bir kopya üretilemesin.
    private EditorSettings() {
        // Uygulama ilk açıldığındaki varsayılan değerler
        this.fontSize = 14;
        this.theme = "Light Theme";
        this.wordWrapEnabled = true;
        this.windowWidth = 1000;
        this.windowHeight = 700;
        this.padding = 20;
        this.autoSavePath = System.getProperty("user.home");
        this.autoSavePeriod = 30;
        this.currentFilePath = null;
        this.highlighterColor = new Color(150, 120, 30);
        this.modified = false;
        this.showLineNumbers = true;
    }
    
    // Sistemin her yerinden ayarlara ulaşmak için kullanılacak tek giriş noktası
    public static EditorSettings getInstance() {
        if (instance == null) {
            // Thread-Safe (İş parçacığı güvenli) Singleton: Double-checked locking
            synchronized (EditorSettings.class) {
                if (instance == null) {
                    instance = new EditorSettings();
                }
            }
        }
        return instance;
    }
    
    // EditorSettings.java içine eklenecek metotlar:

    // Ayarları dosyaya kaydet
    public void saveToProperties() {
        java.util.Properties props = new java.util.Properties();
        
        props.setProperty("theme", this.theme != null ? this.theme : "Dark Theme");
        props.setProperty("fontSize", String.valueOf(this.fontSize));
        props.setProperty("wordWrapEnabled", String.valueOf(this.wordWrapEnabled));
        props.setProperty("padding", String.valueOf(this.padding));
        props.setProperty("autoSavePeriod", String.valueOf(this.autoSavePeriod));
        props.setProperty("autoSavePath", this.autoSavePath != null ? this.autoSavePath : "");
        
        // Rengi int olarak (RGB) saklamak en güvenlisidir
        if (this.highlighterColor != null) {
            props.setProperty("highlighterColor", String.valueOf(this.highlighterColor.getRGB()));
        }

        try (java.io.FileOutputStream out = new java.io.FileOutputStream("settings.properties")) {
            props.store(out, "TextEditor Ayarlari");
        } catch (Exception e) {
            System.err.println("Ayarlar kaydedilemedi: " + e.getMessage());
        }
    }

    // Uygulama açılırken ayarları yükle
    public void loadFromProperties() {
        java.io.File file = new java.io.File("settings.properties");
        if (file.exists()) {
            try (java.io.FileInputStream in = new java.io.FileInputStream(file)) {
                java.util.Properties props = new java.util.Properties();
                props.load(in);

                this.theme = props.getProperty("theme", "Dark Theme");
                this.fontSize = Integer.parseInt(props.getProperty("fontSize", "14"));
                this.wordWrapEnabled = Boolean.parseBoolean(props.getProperty("wordWrapEnabled", "true"));
                this.padding = Integer.parseInt(props.getProperty("padding", "10"));
                this.autoSavePeriod = Integer.parseInt(props.getProperty("autoSavePeriod", "30"));
                this.autoSavePath = props.getProperty("autoSavePath", "");
                
                String colorStr = props.getProperty("highlighterColor");
                if (colorStr != null) {
                    this.highlighterColor = new java.awt.Color(Integer.parseInt(colorStr));
                }

            } catch (Exception e) {
                System.err.println("Ayarlar yuklenemedi, varsayilanlar kullanilacak: " + e.getMessage());
            }
        }
    }
    
    // --- Getters ve Setters ---

    public int getFontSize() { return fontSize; }
    public void setFontSize(int fontSize) { this.fontSize = fontSize; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public boolean isWordWrapEnabled() { return wordWrapEnabled; }
    public void setWordWrapEnabled(boolean wordWrapEnabled) { this.wordWrapEnabled = wordWrapEnabled; }
    
    public int getWindowWidth() { return windowWidth; }
    public void setWindowWidth(int windowWidth) { this.windowWidth = windowWidth; }

    public int getWindowHeight() { return windowHeight; }
    public void setWindowHeight(int windowHeight) { this.windowHeight = windowHeight; }

    public int getPadding() { return padding; }
    public void setPadding(int padding) { this.padding = padding; }
    
    public String getAutoSavePath() { return autoSavePath; }
    public void setAutoSavePath(String autoSavePath) { this.autoSavePath = autoSavePath; }
    
    public int getAutoSavePeriod() { return autoSavePeriod; }
    public void setAutoSavePeriod(int autoSavePeriod) { this.autoSavePeriod = autoSavePeriod; }
    
    public String getCurrentFilePath() { return currentFilePath; }
    public void setCurrentFilePath(String currentFilePath) { this.currentFilePath = currentFilePath; }

    public Color getHighlighterColor() { return highlighterColor; }
    public void setHighlighterColor(Color highlighterColor) { this.highlighterColor = highlighterColor; }
    
    public boolean isModified() { return modified; }
    public void setModified(boolean modified) { this.modified = modified; }
    
    public String getCurrentFileName() {
        if (currentFilePath == null) return "Yeni Belge";
        return new java.io.File(currentFilePath).getName();
    }
    
    public boolean isShowLineNumbers() { return showLineNumbers; }
    public void setShowLineNumbers(boolean showLineNumbers) { this.showLineNumbers = showLineNumbers; }

}
