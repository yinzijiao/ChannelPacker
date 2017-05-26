package com.packer.tool;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.packer.ui.JsonFormatUI;
import org.jetbrains.annotations.NotNull;

/**
 * Created by yin on 2017/5/26.
 */
public class JsonWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        JsonFormatUI formatUI = new JsonFormatUI();
        toolWindow.getComponent().add(formatUI.getjPanel());
    }

}
