package com.packer.ui;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.ConfigurableUi;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import com.packer.Constants;
import com.packer.config.Setting;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by yin on 2017/5/18.
 */
public class SettingUI implements ConfigurableUi<Setting> {

    private JComboBox comboBox1;
    private JPanel jPanel;
    private JLabel hint;
    private JTextField jPassword;
    private JLabel password;
    private JLabel keystore;
    private JLabel apktools;
    private JTextField jAlias;
    private JLabel alias;
    private TextFieldWithBrowseButton jFlavor;
    private TextFieldWithBrowseButton jKeystore;
    private TextFieldWithBrowseButton jApktools;

    public SettingUI() {
        comboBox1.addItem(Constants.CHANNEL_TYPE_META_INF);
        comboBox1.addItem(Constants.CHANNEL_TYPE_MANIFEST);

        jFlavor.addBrowseFolderListener("选择", null, null, new FileChooserDescriptor(true, false, true, false, false, false), TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT, false);
        jApktools.addBrowseFolderListener("选择", null, null, new FileChooserDescriptor(true, false, true, false, false, false), TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT, false);
        jKeystore.addBrowseFolderListener("选择", null, null, new FileChooserDescriptor(true, false, true, false, false, false), TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT, false);

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
            jFlavor.setText(setting.getState().getChannelFile());
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
        return !setting.getState().getChannelFile().equals(jFlavor.getText().toString());
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
        setting.getState().setChannelFile(jFlavor.getText().toString());
    }

    private void applyType(@NotNull Setting setting) {
        setting.getState().setChannelType(comboBox1.getSelectedItem().toString());
    }
}
