package com.test.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.test.Constants;
import com.test.config.Setting;
import com.test.utils.Logger;

/**
 * Created by yin on 2017/5/18.
 */
public class ChannelAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        VirtualFile virtualFile = e.getData(LangDataKeys.VIRTUAL_FILE);
        if (!virtualFile.isDirectory() && virtualFile.getName().endsWith(".apk")) {
            if (Constants.CHANNEL_TYPE_ZIP.equals(Setting.state.getChannelType())) {
                packerWIthZip(virtualFile);
            } else {
                packerWIthManifest(virtualFile);
            }
        }
    }

    private void packerWIthManifest(VirtualFile virtualFile) {

    }

    private void packerWIthZip(VirtualFile virtualFile) {

    }
}
