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
    private JTextField jKeystore;
    private JTextField jPassword;
    private JLabel password;
    private JLabel keystore;
    private JTextField jApktools;
    private JLabel apktools;
    private JButton bKeystore;
    private JButton bApktools;
    private JTextField jAlias;
    private JLabel alias;

    public SettingUI() {
        comboBox1.addItem(Constants.CHANNEL_TYPE_META_INF);
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

        bKeystore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.showDialog(new JPanel(), "选择");
                File selectedFile = fileChooser.getSelectedFile();
                if (selectedFile != null) {
                    jKeystore.setText(selectedFile.getAbsolutePath());
                    jKeystore.requestDefaultFocus();
                }
            }
        });
        bApktools.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.showDialog(new JPanel(), "选择");
                File selectedFile = fileChooser.getSelectedFile();
                if (selectedFile != null) {
                    jApktools.setText(selectedFile.getAbsolutePath());
                    jApktools.requestDefaultFocus();
                }
            }
        });

        hint.setForeground(new Color(255, 107, 104));

        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                setHint(e.getItem());
            }
        });
    }

    private void setHint(Object selectedItem) {
        if (Constants.CHANNEL_TYPE_META_INF.equals(selectedItem)) {
            hint.setText("使用META-INF模式请在build.gradle中设置 : v2SigningEnabled false");
            keystore.setVisible(false);
            jKeystore.setVisible(false);
            password.setVisible(false);
            jPassword.setVisible(false);
            apktools.setVisible(false);
            jApktools.setVisible(false);
            bApktools.setVisible(false);
            bKeystore.setVisible(false);
            alias.setVisible(false);
            jAlias.setVisible(false);
        } else {
            hint.setText("");
            keystore.setVisible(true);
            jKeystore.setVisible(true);
            password.setVisible(true);
            jPassword.setVisible(true);
            apktools.setVisible(true);
            jApktools.setVisible(true);
            bApktools.setVisible(true);
            bKeystore.setVisible(true);
            alias.setVisible(true);
            jAlias.setVisible(true);
        }
    }

    @Override
    public void reset(@NotNull Setting setting) {
        resetType(setting);
        resetPasword(setting);
        resetChannelFile(setting);
        resetApktools(setting);
        resetKeystore(setting);
        resetAlias(setting);
    }

    @Override
    public boolean isModified(@NotNull Setting setting) {
        if (Constants.CHANNEL_TYPE_META_INF.equals(comboBox1.getSelectedItem())) {
            return modifiedType(setting) || modifiedChannelFile(setting);
        } else {
            return modifiedType(setting) || modifiedChannelFile(setting) || modifiedApktools(setting) || modifiedKeystore(setting) || modifiedPaswords(setting) || modifiedAlias(setting);
        }
    }

    @Override
    public void apply(@NotNull Setting setting) throws ConfigurationException {
        applyType(setting);
        applyChannelFile(setting);
        applyApktools(setting);
        applyKeystore(setting);
        applyPasword(setting);
        applyAlias(setting);
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return jPanel;
    }

    private void resetType(@NotNull Setting setting) {
        if (StringUtil.isEmpty(setting.getState().getChannelType())) {
            setting.getState().setChannelType(setting.getState().getChannelType());
        }
        comboBox1.setSelectedItem(setting.getState().getChannelType());

        setHint(comboBox1.getSelectedItem());
    }

    private void resetChannelFile(@NotNull Setting setting) {
        if (!StringUtil.isEmpty(setting.getState().getChannelFile())) {
            textField1.setText(setting.getState().getChannelFile());
        }
    }

    private void resetKeystore(@NotNull Setting setting) {
        if (!StringUtil.isEmpty(setting.getState().getKeystore())) {
            jKeystore.setText(setting.getState().getKeystore());
        }
    }


    private void resetApktools(@NotNull Setting setting) {
        if (!StringUtil.isEmpty(setting.getState().getApktools())) {
            jApktools.setText(setting.getState().getApktools());
        }
    }


    private void resetPasword(@NotNull Setting setting) {
        jPassword.setText(setting.getState().getPassword());
    }

    private void resetAlias(@NotNull Setting setting) {
        jAlias.setText(setting.getState().getAlias());
    }

    private boolean modifiedType(@NotNull Setting setting) {
        return !setting.getState().getChannelType().equals(comboBox1.getSelectedItem());
    }

    private boolean modifiedChannelFile(@NotNull Setting setting) {
        return !setting.getState().getChannelFile().equals(textField1.getText().toString());
    }

    private boolean modifiedKeystore(@NotNull Setting setting) {
        return !setting.getState().getKeystore().equals(jKeystore.getText().toString());
    }

    private boolean modifiedApktools(@NotNull Setting setting) {
        return !setting.getState().getApktools().equals(jApktools.getText().toString());
    }

    private boolean modifiedAlias(@NotNull Setting setting) {
        return !setting.getState().getAlias().equals(jAlias.getText().toString());
    }

    private boolean modifiedPaswords(@NotNull Setting setting) {
        return !setting.getState().getPassword().equals(jPassword.getText().toString());
    }

    private void applyPasword(@NotNull Setting setting) {
        setting.getState().setPassword(jPassword.getText().toString());
    }

    private void applyAlias(@NotNull Setting setting) {
        setting.getState().setAlias(jAlias.getText().toString());
    }

    private void applyApktools(@NotNull Setting setting) {
        setting.getState().setApktools(jApktools.getText().toString());
    }

    private void applyKeystore(@NotNull Setting setting) {
        setting.getState().setKeystore(jKeystore.getText().toString());
    }

    private void applyChannelFile(@NotNull Setting setting) {
        setting.getState().setChannelFile(textField1.getText().toString());
    }

    private void applyType(@NotNull Setting setting) {
        setting.getState().setChannelType(comboBox1.getSelectedItem().toString());
    }
}
