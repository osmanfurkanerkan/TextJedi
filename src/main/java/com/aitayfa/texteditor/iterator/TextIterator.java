/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.iterator;
import java.util.Iterator;
/**
 *
 * @author berkaysarmasoglu
 */

// ITERATOR PATTERN
public class TextIterator implements Iterator<String>{
    private String[] lines;
    private int currentLineIndex = 0;
    private String[] currentWords;
    private int currentWordIndex = 0;
    
    // next() çağrıldığında kelimenin asıl bulunduğu satır
    private int lastReturnedLineIndex = 0;
    
    public TextIterator(String text) {
        // Metni önce satır satır bölüyoruz
        lines = text.split("\n");
        loadWordsForCurrentLine();
    }
    
    // O anki satırı kelimelere parçalayıp hazırlayan yardımcı metot
    private void loadWordsForCurrentLine() {
        while (currentLineIndex < lines.length) {
            String line = lines[currentLineIndex].trim();
            if (!line.isEmpty()) {
                currentWords = line.split("\\s+");
                currentWordIndex = 0;
                return; // Dolu bir satır bulduk, aramaya hazır
            }
            // Satır boşsa bir sonrakine geç
            currentLineIndex++; 
        }
        currentWords = null; // Okunacak satır kalmadı
    }
    
    @Override
    public boolean hasNext() {
        return currentWords != null && currentWordIndex < currentWords.length;
    }

    @Override
    public String next() {
        String word = currentWords[currentWordIndex++];
        lastReturnedLineIndex = currentLineIndex; // Kelimenin bulunduğu satırı hafızaya al
        
        // O satırdaki kelimeler bittiyse, bir sonraki satıra geçmek için sistemi kur
        if (currentWordIndex >= currentWords.length) {
            currentLineIndex++;
            loadWordsForCurrentLine();
        }
        
        return word;
    }
    
    public int getCurrentLineNumber() {
        return lastReturnedLineIndex + 1;
    }
}
