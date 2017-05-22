package com.packer.utils;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.Messages;

/**
 * Created by yin on 2017/5/22.
 */
public class UIHelper {

    public static void messageHint(String content){
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                Messages.showMessageDialog(ProjectManager.getInstance().getDefaultProject(), content, "提示", Messages.getInformationIcon());
            }
        });
    }

}
