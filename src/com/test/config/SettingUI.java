package com.test.config;

import com.intellij.openapi.options.ConfigurableUi;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by yin on 2017/5/16.
 */
public class SettingUI implements ConfigurableUi<Setting> {

    private JTextField textField1;
    private JPanel jPanel;

    public SettingUI() {
        initJPanel();
    }

    private void initJPanel() {
        if (!StringUtil.isEmpty(Setting.state.getAdbPath())) {
            textField1.setText(Setting.state.getAdbPath());
        }
    }

    @Override
    public void reset(@NotNull Setting setting) {
        textField1.setText(setting.getState().getAdbPath());
    }

    @Override
    public boolean isModified(@NotNull Setting setting) {
        return !setting.getState().getAdbPath().equals(textField1.getText().toString());
    }

    @Override
    public void apply(@NotNull Setting setting) throws ConfigurationException {
        setting.getState().setAdbPath(textField1.getText().toString());
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return jPanel;
    }
}
