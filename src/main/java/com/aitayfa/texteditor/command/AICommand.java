/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.command;

/**
 *
 * @author berkaysarmasoglu
 */

import javax.swing.*;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.Cursor;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import io.github.cdimascio.dotenv.Dotenv;

public class AICommand implements Command {
    private JFrame parent;
    private JTextArea textArea;
    private UndoManager undoManager;

    public AICommand(JFrame parent, JTextArea textArea, UndoManager undoManager) {
        this.parent = parent;
        this.textArea = textArea;
        this.undoManager = undoManager;
    }

    @Override
    public void execute() {
        String userPrompt = JOptionPane.showInputDialog(parent, "Yapay zekadan ne yapmasını istersiniz?\n(Örn: Bu metni İngilizceye çevir, Yazım hatalarını düzelt vb.)", "AI Yardımı", JOptionPane.QUESTION_MESSAGE);
        if (userPrompt == null || userPrompt.trim().isEmpty()) {
            return; // İptale basıldıysa veya boş girildiyse çık
        }

        String currentText = textArea.getText();
        String systemInstruction = "Sen bir metin editörü arka plan asistanısın. " +
                                   "Görevin, kullanıcının isteği doğrultusunda verilen metni dönüştürmektir. " +
                                   "KURALLAR: " +
                                   "1. KESİNLİKLE açıklama yapma. " +
                                   "2. KESİNLİKLE alternatif seçenekler sunma. " +
                                   "3. 'İşte çevirisi', 'Tabii ki' gibi sohbet cümleleri kurma. " +
                                   "4. Sadece ve sadece dönüştürülmüş/işlenmiş nihai metni çıktı olarak ver.";
        String fullRequest = systemInstruction + "\n\nİstek: " + userPrompt + "\n\nMetin:\n" + currentText;

        // Bekleme imlecini aktif et
        parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        textArea.setEditable(false); // İşlem sürerken araya yazı yazılmasını engelle

        // API çağrısını arka planda yap (SwingWorker)
        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                Dotenv dotenv = Dotenv.load();
                String apiKey = dotenv.get("GOOGLE_API_KEY");
                Client client = Client.builder().apiKey(apiKey).build();
                return
                client.models.generateContent(
                    "gemini-3-flash-preview",
                    fullRequest,
                    null).text();

            }

            @Override
            protected void done() {
                // 4. API'den yanıt geldiğinde burası çalışır (Ana iş parçacığına geri döneriz)
                try {
                    String aiResponse = get(); // doInBackground'dan dönen sonucu alır

                    // --- TEK ADIMDA UNDO (GERİ AL) MANTIĞI ---
                    textArea.getDocument().removeUndoableEditListener(undoManager);
                    textArea.setText(aiResponse);
                    textArea.getDocument().addUndoableEditListener(undoManager);

                    undoManager.addEdit(new AbstractUndoableEdit() {
                        @Override
                        public void undo() throws CannotUndoException {
                            super.undo();
                            textArea.getDocument().removeUndoableEditListener(undoManager);
                            textArea.setText(currentText);
                            textArea.getDocument().addUndoableEditListener(undoManager);
                        }

                        @Override
                        public void redo() throws CannotRedoException {
                            super.redo();
                            textArea.getDocument().removeUndoableEditListener(undoManager);
                            textArea.setText(aiResponse);
                            textArea.getDocument().addUndoableEditListener(undoManager);
                        }
                    });
                    // ------------------------------------------

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parent, "AI Yanıt Verirken Hata Oluştu:\n" + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                } finally {
                    // İşlem bittiğinde veya hata verdiğinde imleci ve editörü normale döndür
                    parent.setCursor(Cursor.getDefaultCursor());
                    textArea.setEditable(true);
                }
            }
        };

        worker.execute(); // Arka plan işlemini başlat
    }

}