/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aitayfa.texteditor.command;
import javax.swing.undo.UndoManager;
import java.awt.Toolkit;

/**
 *
 * @author berkaysarmasoglu
 */
public class UndoCommand implements Command {
    private UndoManager undoManager;

    public UndoCommand(UndoManager undoManager) {
        this.undoManager = undoManager;
    }

    @Override
    public void execute() {
        // Eğer geri alınabilecek bir işlem varsa geri al
        if (undoManager.canUndo()) {
            undoManager.undo();
        } else {
            // Geri alınacak bir şey kalmadıysa macOS/Windows standart hata sesini (Bip) çıkar
            Toolkit.getDefaultToolkit().beep();
        }
    }
}