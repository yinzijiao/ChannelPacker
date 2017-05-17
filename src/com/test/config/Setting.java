package com.test.config;

import com.intellij.openapi.components.*;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.annotations.Tag;
import com.test.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by yin on 2017/5/16.
 */

@State(
        name = "Setting",
        defaultStateAsResource = false,
        storages = {
                @Storage("test_setting.xml")
        }
)
public class Setting implements PersistentStateComponent<Setting.State> {

    public static State state = new State();

    static class State {

        @Tag
        @NotNull
        private String adbPath;

        @NotNull
        public String getAdbPath() {
            return StringUtil.notNullize(adbPath);
        }

        public void setAdbPath(@NotNull String adbPath) {
            this.adbPath = adbPath;
        }
    }

    @Nullable
    @Override
    public State getState() {
        return state;
    }

    @Override
    public void loadState(State state) {
//        XmlSerializerUtil.copyBean(state, this.state);
        this.state = state;
    }

}
