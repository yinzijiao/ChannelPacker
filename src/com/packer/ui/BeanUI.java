package com.packer.ui;

import com.intellij.openapi.application.ApplicationManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BeanUI extends JDialog implements ClipboardOwner {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTextArea textArea1;
    private JScrollPane jScrollPane;

    public BeanUI() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection stringSelection = new StringSelection(textArea1.getText().toString());
                clipboard.setContents(stringSelection, BeanUI.this);
            }
        });
    }

    public void setText(String text) {
        textArea1.setText(text);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ApplicationManager.getApplication().invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        jScrollPane.getVerticalScrollBar().setValue(0);
                    }
                });
            }
        }).start();
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    public static void main(String[] args) {
        BeanUI dialog = new BeanUI();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {

    }
}
