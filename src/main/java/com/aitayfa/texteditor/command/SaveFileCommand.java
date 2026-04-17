/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.command;
import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author berkaysarmasoglu
 */
public class SaveFileCommand implements Command {
    private JFrame parentWindow;
    private JTextArea targetTextArea;
    
    public SaveFileCommand(JFrame parentWindow, JTextArea targetTextArea){
        this.parentWindow = parentWindow;
        this.targetTextArea = targetTextArea;
    }
    
    @Override
    public void execute(){
        JFileChooser fileChooser = new JFileChooser();
        // open yerine save
        int result = fileChooser.showSaveDialog(parentWindow);
        
        if(result == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();
            
            try (FileWriter writer = new FileWriter(selectedFile)) {
                writer.write(targetTextArea.getText());
                JOptionPane.showMessageDialog(parentWindow, "Dosya başarıyla kaydedildi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("+++++++++++++++++");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parentWindow, "Kaydetme hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                System.out.println("-------------------");
            }
        }
    }
    
}
