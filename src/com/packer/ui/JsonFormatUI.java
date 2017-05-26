package com.packer.ui;

import com.google.gson.*;
import com.intellij.ui.treeStructure.Tree;
import com.packer.utils.Logger;
import com.packer.utils.ResUtil;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by yin on 2017/5/26.
 */
public class JsonFormatUI {
    private JPanel jPanel;
    private JButton formatButton;
    private JTextArea textArea1;
    private JTree tree1;
    private JButton clearButton;

    public JsonFormatUI() {
        formatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textArea1.getText().toString();
                textArea1.setText(ResUtil.formatJson(s));
                JsonParser jp = new JsonParser();
                JsonElement je = jp.parse(s);
                String end = "";
                if (je.isJsonArray() || je.isJsonObject()) {
                    if (je.isJsonArray()) {
                        end = " [" + je.getAsJsonArray().size() + "]";
                    } else {
                        end = " {" + je.getAsJsonObject().entrySet().size() + "}";
                    }
                }
                DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(end);
                formatTree(je, treeNode);
                DefaultTreeModel treeModel = new DefaultTreeModel(treeNode);
                tree1.setModel(treeModel);
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea1.setText("");
            }
        });
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode();
        DefaultTreeModel treeModel = new DefaultTreeModel(treeNode);
        tree1.setModel(treeModel);
    }

    private void formatTree(JsonElement je, DefaultMutableTreeNode tree) {
        if (je.isJsonObject()) {
            JsonObject jsonObject = je.getAsJsonObject();
            Iterator<Map.Entry<String, JsonElement>> iterator = jsonObject.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonElement> next = iterator.next();
                String key = next.getKey();
                JsonElement value = next.getValue();
                if (value.isJsonArray() || value.isJsonObject()) {
                    String end = "";
                    if (value.isJsonArray()) {
                        end = " [" + value.getAsJsonArray().size() + "]";
                    } else {
                        end = " {" + value.getAsJsonObject().entrySet().size() + "}";
                    }
                    DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(key + end);
                    tree.add(defaultMutableTreeNode);
                    formatTree(value, defaultMutableTreeNode);
                } else {
                    DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(key + " : " + value);
                    tree.add(defaultMutableTreeNode);
                }
            }
        } else if (je.isJsonArray()) {
            JsonArray jsonArray = je.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement element = jsonArray.get(i);
                if (element.isJsonArray() || element.isJsonObject()) {
                    String end = "";
                    if (element.isJsonArray()) {
                        end = "";
                    } else {
                        end = " " + i + " {" + element.getAsJsonObject().entrySet().size() + "}";
                    }
                    DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(end);
                    tree.add(defaultMutableTreeNode);
                    formatTree(element, defaultMutableTreeNode);
                } else {
                    DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(element.getAsString());
                    tree.add(defaultMutableTreeNode);
                }
            }
        }
    }

    public JPanel getjPanel() {
        return jPanel;
    }
}
