package com.test.action;

import com.intellij.ide.CopyProvider;
import com.intellij.ide.ExporterToTextFile;
import com.intellij.lang.Language;
import com.intellij.lang.StdLanguages;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileFilter;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.Query;
import com.test.utils.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yin on 2017/5/17.
 */
public class ResFindAction extends AnAction {

    List<VirtualFile> files = new ArrayList<>();

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile virtualFile = e.getData(LangDataKeys.VIRTUAL_FILE);
        Document document = e.getData(LangDataKeys.EDITOR).getDocument();
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(LangDataKeys.EDITOR);
        String text = editor.getSelectionModel().getSelectedText();
        Logger.e(text);
        getXml(project.getBaseDir(), project, text);
        for (int i = 0; i < files.size(); i++) {
            Logger.e(files.get(i).getName() + files.get(i).getPath());
        }
    }

    public void getXml(VirtualFile virtualFile, Project project, String key) {
        VfsUtilCore.iterateChildrenRecursively(virtualFile, new VirtualFileFilter() {
            @Override
            public boolean accept(VirtualFile virtualFile) {
                if (virtualFile.isDirectory()) {
                    return true;
                } else if (virtualFile.getName().endsWith(".xml")) {
                    PsiFile file = PsiManager.getInstance(project).findFile(virtualFile);
                    XmlFile xmlFile = (XmlFile) file.getContainingFile();
                    if ("resources".equals(xmlFile.getRootTag().getName())) {
                        for (int i = 0; i < xmlFile.getRootTag().getSubTags().length; i++) {
                            if (key.equals(xmlFile.getRootTag().getSubTags()[i].getValue().getText())) {
                                Query<PsiReference> search = ReferencesSearch.search(xmlFile.getRootTag().getSubTags()[i]);
                                files.add(virtualFile);
                            }
                        }
                    }
                }
                return false;
            }
        }, new ContentIterator() {
            @Override
            public boolean processFile(VirtualFile virtualFile) {
                if (virtualFile.isDirectory()) {
                    for (int i = 0; i < virtualFile.getChildren().length; i++) {
                        getXml(virtualFile.getChildren()[i], project, key);
                    }
                }
                return false;
            }
        });
    }
}
