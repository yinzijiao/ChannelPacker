package com.packer.utils;

import com.esotericsoftware.minlog.Log;
import com.intellij.openapi.project.ProjectManager;

/**
 * Created by yin on 2017/5/16.
 */
public class Logger {

    public static void i(String msg) {
        Log.info(ProjectManager.getInstance().getOpenProjects()[0].getName(), msg);
    }

    public static void d(String msg) {
        Log.debug(ProjectManager.getInstance().getOpenProjects()[0].getName(), msg);
    }

    public static void w(String msg) {
        Log.warn(ProjectManager.getInstance().getOpenProjects()[0].getName(), msg);
    }

    public static void e(String msg) {
        Log.error(ProjectManager.getInstance().getOpenProjects()[0].getName(), msg);
    }

    public static void t(String msg) {
        Log.trace(ProjectManager.getInstance().getOpenProjects()[0].getName(), msg);
    }

}
