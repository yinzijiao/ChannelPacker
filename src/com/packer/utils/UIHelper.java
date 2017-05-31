package com.packer.utils;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.JBColor;
import com.intellij.ui.awt.RelativePoint;

import javax.swing.*;
import javax.swing.event.HyperlinkListener;
import java.awt.*;

/**
 * Created by yin on 2017/5/22.
 */
public class UIHelper {

    public static void messageHint(String content) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                Messages.showMessageDialog(ProjectManager.getInstance().getDefaultProject(), content, "提示", Messages.getInformationIcon());
            }
        });
    }

    public static void showPopupBalloon(final Editor editor, final String result) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                JBPopupFactory factory = JBPopupFactory.getInstance();
                factory.createHtmlTextBalloonBuilder(result, null, new JBColor(new Color(186, 238, 186), new Color(73, 117, 73)), null)
                        .setFadeoutTime(5000)
                        .createBalloon()
                        .show(factory.guessBestPopupLocation(editor), Balloon.Position.below);
            }
        });
    }

    public static void makeToast(Project project, JComponent jComponent, MessageType type, String text) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
                JBPopupFactory.getInstance().createHtmlTextBalloonBuilder(text, type, (HyperlinkListener) null).setFadeoutTime(7500L).createBalloon().show(RelativePoint.getCenterOf(jComponent), Balloon.Position.above);
            }
        });
    }

    public static void makeToast(Project project, MessageType type, String text) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
                JBPopupFactory.getInstance().createHtmlTextBalloonBuilder(text, type, (HyperlinkListener) null).setFadeoutTime(7500L).createBalloon().show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
            }
        });
    }

}
