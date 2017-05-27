package com.packer.ui;

import javax.swing.*;
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

    public JTextArea getTextArea1() {
        return textArea1;
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
