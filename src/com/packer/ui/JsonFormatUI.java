package com.packer.ui;

import com.google.gson.*;
import com.intellij.codeInsight.daemon.impl.analysis.JavaGenericsUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.ui.treeStructure.Tree;
import com.packer.utils.ClassMake;
import com.packer.utils.Logger;
import com.packer.utils.ResUtil;
import net.sf.cglib.beans.BeanMap;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
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
    private JButton beanButton;
    private JScrollPane leftScroll;
    private JScrollPane rightScroll;

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

        beanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textArea1.getText().toString();
                textArea1.setText(ResUtil.formatJson(s));
                JsonParser jp = new JsonParser();
                JsonElement je = jp.parse(s);
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(ClassMake.makePublicClass("JavaBean"));
                createJavaBean(je, stringBuffer, "", 0);
                stringBuffer.append(ClassMake.CLASS_END);

                BeanUI dialog = new BeanUI();
                dialog.setText(stringBuffer.toString());
                Toolkit kit = Toolkit.getDefaultToolkit(); //定义工具包
                Dimension screenSize = kit.getScreenSize(); //获取屏幕的尺寸
                int screenWidth = screenSize.width; //获取屏幕的宽
                int screenHeight = screenSize.height; //获取屏幕的高
                dialog.setBounds(screenWidth / 2 - 500, screenHeight / 2 - 350, 1000, 700);
                dialog.setLocationRelativeTo(null);
                dialog.pack();
                dialog.setSize(1000, 700);
                dialog.setVisible(true);
            }
        });
    }

    private void createJavaBean(JsonElement je, StringBuffer sb, String k, int count) {
        count++;
        if (je.isJsonObject()) {
            JsonObject jsonObject = je.getAsJsonObject();
            Iterator<Map.Entry<String, JsonElement>> iterator = jsonObject.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonElement> next = iterator.next();
                String key = next.getKey();
                JsonElement value = next.getValue();
                if (value.isJsonObject()) {
                    sb.append(ClassMake.makeField(key, count));
                    sb.append(ClassMake.makeClass(key, count));
                    createJavaBean(value, sb, key, count);
                    sb.append(ClassMake.tab(count));
                    sb.append(ClassMake.CLASS_END);
                    sb.append(ClassMake.makeGet(key, ClassMake.getClazz(value, key), count));
                    sb.append(ClassMake.makeSet(key, ClassMake.getClazz(value, key), count));
                } else if (value.isJsonArray()) {
                    if (!sb.toString().contains(ClassMake.LIST_IMPORT)) {
                        sb.insert(0, ClassMake.LIST_IMPORT);
                    }
                    try {
                        sb.append(ClassMake.makeFieldList(key, ClassMake.getClazz(value.getAsJsonArray().get(0), key), count));
                        createJavaBean(value, sb, key, count);
                        sb.append(ClassMake.makeGetList(key, ClassMake.getClazz(value.getAsJsonArray().get(0), key), count));
                        sb.append(ClassMake.makeSetList(key, ClassMake.getClazz(value.getAsJsonArray().get(0), key), count));
                    } catch (Exception e) {
                    }
                } else {
                    sb.append(ClassMake.makeField(key, ClassMake.getClazz(value, key), count));
                    sb.append(ClassMake.makeGet(key, ClassMake.getClazz(value, key), count));
                    sb.append(ClassMake.makeSet(key, ClassMake.getClazz(value, key), count));
                }
            }
        } else if (je.isJsonArray()) {
            if (!sb.toString().contains(ClassMake.LIST_IMPORT)) {
                sb.insert(0, ClassMake.LIST_IMPORT);
            }
            JsonArray jsonArray = je.getAsJsonArray();
            try {
                JsonElement element = jsonArray.get(0);
                if (element.isJsonObject()) {
                    sb.append(ClassMake.makeClass(ClassMake.getClazz(element.getAsJsonObject(), k), count - 1));
                    createJavaBean(element, sb, k, count - 1);
                    sb.append(ClassMake.tab(count - 1));
                    sb.append(ClassMake.CLASS_END);
                }
            } catch (Exception e) {
            }
        } else {

        }
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
