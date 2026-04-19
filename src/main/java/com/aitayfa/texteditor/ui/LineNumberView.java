/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.ui;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 *
 * @author berkaysarmasoglu
 */
public class LineNumberView extends JComponent {
    private JTextArea textArea;

    public LineNumberView(JTextArea textArea) {
        this.textArea = textArea;
        setFont(textArea.getFont()); 

        // Metin değiştikçe numaraları yeniden çiz
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { repaint(); }
            @Override public void removeUpdate(DocumentEvent e) { repaint(); }
            @Override public void changedUpdate(DocumentEvent e) { repaint(); }
        });
        
        // Kullanıcı ayarlardan fontu büyüttüğünde numaratörün de büyümesi için:
        textArea.addPropertyChangeListener("font", evt -> {
            setFont(textArea.getFont());
            repaint();
        });
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Sadece clip alanını değil, tüm bileşeni boya
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(getForeground());
        FontMetrics fm = g.getFontMetrics();
        int fontHeight = fm.getHeight();
        int fontAscent = fm.getAscent();

        // TextArea'nın padding (kenar boşluğu) ayarını hesaba kat
        Insets insets = textArea.getInsets();
        int startY = insets.top;

        int lineCount = textArea.getLineCount();
        for (int i = 0; i < lineCount; i++) {
            String lineNum = String.valueOf(i + 1);
            int y = startY + (i * fontHeight) + fontAscent;
            int x = getWidth() - fm.stringWidth(lineNum) - 8;
            g.drawString(lineNum, x, y);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        // Yüksekliği 0 kalmaktan kurtaran sihirli satır:
        int height = Math.max(textArea.getHeight(), textArea.getPreferredSize().height);
        return new Dimension(45, height);
    }
}
