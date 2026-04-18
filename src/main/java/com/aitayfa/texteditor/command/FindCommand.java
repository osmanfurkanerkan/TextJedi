package com.aitayfa.texteditor.command;

import com.aitayfa.texteditor.iterator.TextIterator;
import com.aitayfa.texteditor.config.EditorSettings;
import javax.swing.*;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.Color;

public class FindCommand implements Command {
    private JTextArea textArea;
    private JFrame parent;

    public FindCommand(JFrame parent, JTextArea textArea) {
        this.parent = parent;
        this.textArea = textArea;
    }

    @Override
    public void execute() {
        String searchTerm = JOptionPane.showInputDialog(parent, "Aranacak kelimeyi girin:");
        
        // 1. Önceki aramalardan kalan eski vurguları temizle
        Highlighter highlighter = textArea.getHighlighter();
        highlighter.removeAllHighlights();
        if (searchTerm == null || searchTerm.isEmpty()) return;

        
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(EditorSettings.getInstance().getHighlighterColor());

        TextIterator iterator = new TextIterator(textArea.getText());
        int count = 0;

        try {
            while (iterator.hasNext()) {
                String currentWord = iterator.next();
                
                // Kelime eşleşiyorsa, Iterator'dan indeksleri al ve o aralığı boya
                if (currentWord.equalsIgnoreCase(searchTerm)) {
                    highlighter.addHighlight(iterator.getStartIndex(), iterator.getEndIndex(), painter);
                    count++;
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "Vurgulama hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }

        // 3. Kullanıcıya sonucu bildir
        if (count > 0) {
            JOptionPane.showMessageDialog(parent, 
                count + " adet eşleşme bulundu ve işaretlendi.", 
                "Arama Tamamlandı", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parent, 
                "Maalesef '" + searchTerm + "' kelimesi bulunamadı.", 
                "Sonuç Yok", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
}