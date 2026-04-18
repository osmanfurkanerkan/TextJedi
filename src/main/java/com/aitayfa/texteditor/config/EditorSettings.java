/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.config;
import java.io.File;
import java.awt.Color;
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
    
    // Constructor'ı private yapıyoruz ki dışarıdan 'new' anahtar kelimesiyle başka bir kopya üretilemesin.
    private EditorSettings() {
        // Uygulama ilk açıldığındaki varsayılan değerler
        this.fontSize = 14;
        this.theme = "Light Theme";
        this.wordWrapEnabled = true;
        this.windowWidth = 1000;
        this.windowHeight = 700;
        this.padding = 20;
        this.autoSavePath = System.getProperty("user.home") + File.separator + "texteditor_autosave.txt";
        this.autoSavePeriod = 30000;
        this.currentFilePath = null;
        this.highlighterColor = new Color(150, 120, 30);
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

}
