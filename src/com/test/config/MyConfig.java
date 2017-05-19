package com.test.config;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.ConfigurableBase;
import com.test.component.MyComponent;
import com.test.ui.SettingUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by yin on 2017/5/15.
 */
public class MyConfig extends ConfigurableBase<SettingUI, Setting> {

    public MyConfig(@NotNull String id, @NotNull String displayName, @Nullable String helpTopic) {
        super(id, displayName, helpTopic);
    }

    @NotNull
    @Override
    protected Setting getSettings() {
        return MyComponent.getInstance().getSetting();
    }

    @Override
    protected SettingUI createUi() {
        return new SettingUI();
    }
}
