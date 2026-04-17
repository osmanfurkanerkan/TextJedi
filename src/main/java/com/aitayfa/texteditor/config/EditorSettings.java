/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.config;
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
    
    // Constructor'ı private yapıyoruz ki dışarıdan 'new' anahtar kelimesiyle başka bir kopya üretilemesin.
    private EditorSettings() {
        // Uygulama ilk açıldığındaki varsayılan değerler
        this.fontSize = 14;
        this.theme = "Light";
        this.wordWrapEnabled = true;
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

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean isWordWrapEnabled() {
        return wordWrapEnabled;
    }

    public void setWordWrapEnabled(boolean wordWrapEnabled) {
        this.wordWrapEnabled = wordWrapEnabled;
    }
    
}
