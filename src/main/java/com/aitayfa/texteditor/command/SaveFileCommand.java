/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.command;
import com.aitayfa.texteditor.config.EditorSettings;
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
    public void execute() {
        String currentPath = EditorSettings.getInstance().getCurrentFilePath();

        if (currentPath == null) {
            // Yeni belge: Farklı Kaydet mantığını çalıştır
            new SaveAsFileCommand(parentWindow, targetTextArea).execute();
        } else {
            // Var olan dosya: Sessizce üzerine yaz
            new SaveAsFileCommand(parentWindow, targetTextArea).saveToFile(new File(currentPath));
        }
    }
}
