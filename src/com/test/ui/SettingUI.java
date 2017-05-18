package com.test.ui;

import com.intellij.openapi.options.ConfigurableUi;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.text.StringUtil;
import com.test.Constants;
import com.test.config.Setting;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by yin on 2017/5/18.
 */
public class SettingUI implements ConfigurableUi<Setting> {

    private JComboBox comboBox1;
    private JPanel jPanel;

    public SettingUI() {
        comboBox1.addItem(Constants.CHANNEL_TYPE_ZIP);
        comboBox1.addItem(Constants.CHANNEL_TYPE_MANIFEST);
    }

    @Override
    public void reset(@NotNull Setting setting) {
        if (StringUtil.isEmpty(setting.getState().getChannelType())) {
            setting.getState().setChannelType(comboBox1.getItemAt(0).toString());
        }
        comboBox1.setSelectedItem(setting.getState().getChannelType());
    }

    @Override
    public boolean isModified(@NotNull Setting setting) {
        return !setting.getState().getChannelType().equals(comboBox1.getSelectedItem());
    }

    @Override
    public void apply(@NotNull Setting setting) throws ConfigurationException {
        setting.getState().setChannelType(comboBox1.getSelectedItem().toString());
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return jPanel;
    }
}
