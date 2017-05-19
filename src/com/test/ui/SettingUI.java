package com.test.ui;

import com.intellij.openapi.options.ConfigurableUi;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.text.StringUtil;
import com.test.Constants;
import com.test.config.Setting;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

/**
 * Created by yin on 2017/5/18.
 */
public class SettingUI implements ConfigurableUi<Setting> {

    private JComboBox comboBox1;
    private JPanel jPanel;
    private JTextField textField1;
    private JButton button1;
    private JLabel hint;

    public SettingUI() {
        comboBox1.addItem(Constants.CHANNEL_TYPE_ZIP);
        comboBox1.addItem(Constants.CHANNEL_TYPE_MANIFEST);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.showDialog(new JPanel(), "选择");
                File selectedFile = fileChooser.getSelectedFile();
                if (selectedFile != null) {
                    textField1.setText(selectedFile.getAbsolutePath());
                    textField1.requestDefaultFocus();
                }
            }
        });

        hint.setForeground(Color.red);

        if (Constants.CHANNEL_TYPE_ZIP.equals(comboBox1.getSelectedItem())) {
            hint.setText("使用zip模式请在build.gradle中设置:v2SigningEnabled false");
        } else {
            hint.setText("");
        }

        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (Constants.CHANNEL_TYPE_ZIP.equals(e.getItem())) {
                    hint.setText("使用zip模式请在build.gradle中设置:v2SigningEnabled false");
                } else {
                    hint.setText("");
                }
            }
        });
    }

    @Override
    public void reset(@NotNull Setting setting) {
        if (StringUtil.isEmpty(setting.getState().getChannelType())) {
            setting.getState().setChannelType(comboBox1.getItemAt(0).toString());
        }
        comboBox1.setSelectedItem(setting.getState().getChannelType());

        if (!StringUtil.isEmpty(setting.getState().getChannelFile())) {
            textField1.setText(setting.getState().getChannelFile());
        }
    }

    @Override
    public boolean isModified(@NotNull Setting setting) {
        return !setting.getState().getChannelType().equals(comboBox1.getSelectedItem()) || !setting.getState().getChannelFile().equals(textField1.getText().toString());
    }

    @Override
    public void apply(@NotNull Setting setting) throws ConfigurationException {
        setting.getState().setChannelType(comboBox1.getSelectedItem().toString());
        setting.getState().setChannelFile(textField1.getText().toString());
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return jPanel;
    }
}
