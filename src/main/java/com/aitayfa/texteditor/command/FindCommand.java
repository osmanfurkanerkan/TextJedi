/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.command;
import com.aitayfa.texteditor.iterator.TextIterator;
import javax.swing.*;
/**
 *
 * @author berkaysarmasoglu
 */
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
        if (searchTerm == null || searchTerm.isEmpty()) return;

        TextIterator iterator = new TextIterator(textArea.getText());
        StringBuilder resultMessage = new StringBuilder(); // Sonuçları biriktireceğimiz metin
        int count = 0;

        while (iterator.hasNext()) {
            String currentWord = iterator.next();
            // Kelimelerin yanındaki noktalama işaretlerinden kurtulup saf kelimeyi karşılaştırıyoruz
            String cleanWord = currentWord.replaceAll("[^a-zA-Z0-9çğıöşüÇĞİÖŞÜ]", "");
            
            if (cleanWord.equalsIgnoreCase(searchTerm)) {
                count++;
                resultMessage.append("Satır ").append(iterator.getCurrentLineNumber()).append("\n");
            }
        }

        if (count > 0) {
            JOptionPane.showMessageDialog(parent, 
                "'" + searchTerm + "' kelimesi " + count + " kez bulundu:\n\n" + resultMessage.toString(), 
                "Arama Sonucu", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parent, "Maalesef '" + searchTerm + "' metin içinde bulunamadı.", "Sonuç Yok", JOptionPane.WARNING_MESSAGE);
        }
    }
}
