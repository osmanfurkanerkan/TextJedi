package com.aitayfa.texteditor.iterator;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextIterator implements Iterator<String> {
    private Matcher matcher;
    private boolean hasNextMatch;
    private int lastStartIndex = -1;
    private int lastEndIndex = -1;

    public TextIterator(String text) {
        // Sadece harf ve rakamlardan oluşan kelimeleri bulur (Türkçe karakterler dahil)
        Pattern pattern = Pattern.compile("[a-zA-Z0-9çğıöşüÇĞİÖŞÜ]+");
        matcher = pattern.matcher(text);
        
        // İlk eşleşmeyi bul ve hazırda beklet
        hasNextMatch = matcher.find();
    }

    @Override
    public boolean hasNext() {
        return hasNextMatch;
    }

    @Override
    public String next() {
        String word = matcher.group();
        lastStartIndex = matcher.start(); // Kelimenin başladığı tam karakter indeksi
        lastEndIndex = matcher.end();     // Kelimenin bittiği tam karakter indeksi
        
        hasNextMatch = matcher.find(); // Bir sonraki kelimeyi kontrol et
        return word;
    }

    // Vurgulayıcı (Highlighter) için gerekli olan tam koordinatlar
    public int getStartIndex() { return lastStartIndex; }
    public int getEndIndex() { return lastEndIndex; }
}