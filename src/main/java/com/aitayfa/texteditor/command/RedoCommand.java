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

public class RedoCommand implements Command {
    private UndoManager undoManager;

    public RedoCommand(UndoManager undoManager) {
        this.undoManager = undoManager;
    }

    @Override
    public void execute() {
        if (undoManager.canRedo()) {
            undoManager.redo();
        } else {
            // Yapılacak bir işlem yoksa bildirim
            Toolkit.getDefaultToolkit().beep();
        }
    }
}