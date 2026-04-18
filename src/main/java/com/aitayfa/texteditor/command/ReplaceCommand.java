/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.command;
import javax.swing.*;
import java.util.regex.Pattern;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 *
 * @author berkaysarmasoglu
 */
public class ReplaceCommand implements Command {
    private JFrame parent;
    private JTextArea textArea;
    private UndoManager undoManager;

    public ReplaceCommand(JFrame parent, JTextArea textArea, UndoManager undoManager) {
        this.parent = parent;
        this.textArea = textArea;
        this.undoManager = undoManager;
    }

    @Override
    public void execute() {
        textArea.getHighlighter().removeAllHighlights();

        String targetWord = JOptionPane.showInputDialog(parent, "Değiştirilecek kelimeyi girin:");
        if (targetWord == null || targetWord.isEmpty()) return;

        String newWord = JOptionPane.showInputDialog(parent, "Yeni kelimeyi girin:");
        if (newWord == null) return; // İptale basılmış olabilir

        String currentText = textArea.getText();

        // Küçük/büyük harf duyarsız (case-insensitive) olarak metnin içinde var mı diye kontrol et
        if (!currentText.toLowerCase().contains(targetWord.toLowerCase())) {
            JOptionPane.showMessageDialog(parent, 
                "'" + targetWord + "' kelimesi metinde bulunamadı.", 
                "İşlem İptal Edildi", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // (?i) = Case Insensitive çalışmasını sağlar. 
        // Pattern.quote = Kullanıcı + veya * gibi özel karakterler girerse kodun patlamasını engeller.
        String updatedText = currentText.replaceAll("(?i)" + Pattern.quote(targetWord), newWord);

        // sil + yaz = 2 işlem yerine tek işlem undo manager ile
        textArea.getDocument().removeUndoableEditListener(undoManager);
        textArea.setText(updatedText);  
        textArea.getDocument().addUndoableEditListener(undoManager);
        undoManager.addEdit(new AbstractUndoableEdit() {
            @Override
            public void undo() throws CannotUndoException {
                super.undo();
                // Geri ala basıldığında sessizce eski metne dön
                textArea.getDocument().removeUndoableEditListener(undoManager);
                textArea.setText(currentText); 
                textArea.getDocument().addUndoableEditListener(undoManager);
            }

            @Override
            public void redo() throws CannotRedoException {
                super.redo();
                // İleri ala (Redo) basıldığında sessizce yeni metne dön
                textArea.getDocument().removeUndoableEditListener(undoManager);
                textArea.setText(updatedText); 
                textArea.getDocument().addUndoableEditListener(undoManager);
            }
        });
        
        JOptionPane.showMessageDialog(parent, 
            "Değiştirme işlemi başarıyla tamamlandı.", 
            "Başarılı", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}