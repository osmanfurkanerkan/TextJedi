/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.ui.factory;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
/**
 *
 * @author berkaysarmasoglu
 */

// CONCRETE FACTORY
public class LightUIFactory implements UIFactory{
    
    @Override
    public JButton createButton(String text){
        JButton button = new JButton(text);
        button.setBackground(new Color(230,230,230)); // açık gri
        button.setForeground(Color.BLACK); // siyah yazı
        button.setFocusPainted(false);
        button.setOpaque(true); // OS çizimlerini engellemek için
        button.setBorderPainted(false);
        return button;
    }
    
    @Override
    public JPanel createPanel(){
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE); // beyaz 
        return panel;
    }
    
    @Override
    public JTextArea createTextArea(){
        JTextArea textArea = new JTextArea();
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(Color.BLACK);
        textArea.setCaretColor(Color.BLACK); // İmleç rengi
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        return textArea;
    }
    
    @Override
    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(240, 240, 240));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        menuBar.setOpaque(true);
        menuBar.setBorder(BorderFactory.createEmptyBorder()); // Varsayılan kenarlığı kaldır
        return menuBar;
    }

   @Override
    public JMenu createMenu(String text) {
        JMenu menu = new JMenu(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                
                // Metinlerin daha pürüzsüz (antialiased) görünmesi için
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // 1. Arkaplanı boya
                if (isSelected() || isArmed()) {
                    g2.setColor(new Color(220, 220, 220)); // tıklandığında
                } else {
                    g2.setColor(new Color(230, 230, 230)); // normalde
                }
                g2.fillRect(0, 0, getWidth(), getHeight()); 
                
                g2.setColor(Color.BLACK);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                
                // Metni dikeyde ortalayıp yatayda 10px boşluk vererek yazdırıyoruz
                int x = 10; 
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);
                
                g2.dispose();
            }
        };
        
        menu.setForeground(Color.WHITE);
        // Mac'te boyamanın çalışması için bunların kapatılması ŞARTTIR
        menu.setOpaque(false); // Mac'in kendi opak çizimini engeller
        menu.setContentAreaFilled(false); 
        
        menu.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); 
        
        // Açılır menü ayarları aynı kalıyor
        menu.getPopupMenu().setBackground(new Color(230, 230, 230));
        menu.getPopupMenu().setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        return menu;
    }

    @Override
    public JMenuItem createMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setBackground(new Color(230, 230, 230)); // Hafif daha koyu beyazs
        item.setForeground(Color.BLACK);
        item.setOpaque(true);
        
        // OS kenarlığı yerine özel kenarlık
        item.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10)); 
        return item;
    }

    @Override
    public Border createBorder() {
        // açık gri kenarlık
        return BorderFactory.createLineBorder(new Color(200, 200, 200), 1);
    }
    
}
