package com.test.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiFile;
import com.test.Constants;
import com.test.config.Setting;
import com.test.utils.Logger;

import java.io.File;
import java.net.URI;

/**
 * Created by yin on 2017/5/18.
 */
public class ChannelAction extends AnAction {

    private Project project;

    @Override
    public void actionPerformed(AnActionEvent e) {
        project = e.getProject();
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
        if (StringUtil.isEmpty(Setting.state.getChannelFile())) {
            Messages.showMessageDialog(project, "请在设置中配置正确的路径", "提示", Messages.getInformationIcon());
            return;
        }
        VirtualFile fileByIoFile = LocalFileSystem.getInstance().findFileByIoFile(new File(Setting.state.getChannelFile()));
        if (!fileByIoFile.exists()) {
            Messages.showMessageDialog(project, "请在设置中配置正确的路径", "提示", Messages.getInformationIcon());
            return;
        }

        Document document = FileDocumentManager.getInstance().getDocument(fileByIoFile);
        String text = document.getText();
        String[] channels = text.split("\n");
        for (int i = 0; i < channels.length; i++) {
            String name;
            String value;
            String[] strings = channels[i].split(" ");
            if (strings.length == 2) {
                name = strings[0];
                value = strings[1];
            } else if (strings.length == 3) {
                name = strings[1];
                value = strings[2];
            } else {
                Messages.showMessageDialog(project, "请按正确的规则配置渠道名", "提示", Messages.getInformationIcon());
                return;
            }

            virtualFile.getPath();

        }
    }
}
