package com.packer.component;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ServiceManager;
import com.packer.config.Setting;
import org.jetbrains.annotations.NotNull;

/**
 * Created by yin on 2017/5/15.
 */
public class MyComponent implements ApplicationComponent {

    private Setting setting;
    private static MyComponent component;

    public Setting getSetting() {
        if (setting == null) {
            setting = ServiceManager.getService(Setting.class);
        }
        return setting;
    }

    public MyComponent() {
        component = this;
        setting = ServiceManager.getService(Setting.class);
    }

    public static MyComponent getInstance() {
        return component;
    }

    @Override
    public void initComponent() {
        // TODO: insert component initialization logic here
    }

    @Override
    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "MyComponent";
    }
}
