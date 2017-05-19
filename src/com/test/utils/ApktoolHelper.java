package com.test.utils;

import java.io.*;
import java.util.ArrayList;

public class ApktoolHelper {

    private String apkName;
    private String curPath;
    private String keyFile;
    private String keyPasswd;
    private String apktools;
    private String alias;

    public ApktoolHelper(String apkName, String curPath, String keyFile, String keyPasswd, String apktools, String alias) {
        this.apkName = apkName;
        this.curPath = curPath;
        this.keyFile = keyFile;
        this.keyPasswd = keyPasswd;
        this.apktools = apktools;
        this.alias = alias;
    }

    /**
     * apktool解压apk，替换渠道值
     *
     * @throws Exception
     */
    public void modifyXudao(String channelName, String channelValue, String channel) {
        // 解压 /C 执行字符串指定的命令然后终断    
        String cmdUnpack = "cmd.exe /C java -jar " + apktools + " d -f -s "
                + apkName;
        runtimeExec(cmdUnpack, new File(curPath));          //执行指令 cmd指令
        System.out.println("==INFO 2.==解压apk成功，准备移动======");

        // 备份AndroidManifest.xml    
        // 获取解压的apk文件名    
        String[] apkFilePath = apkName.split("\\\\");
        String shortApkName = apkFilePath[apkFilePath.length - 1];
        System.out.println("shortApkName = " + shortApkName);

        String dir = shortApkName.substring(0, shortApkName.length() - 4);
        System.err.println("dir = " + dir);
        File packDir = new File(dir);   //获得解压的apk目录

        String f_mani = packDir.getAbsolutePath() + "\\AndroidManifest.xml";
        String f_mani_bak = curPath + "\\AndroidManifest.xml";
        //在当前文件夹下新建一个AndroidManifest.xml文件并把解压的apk文件里面的AndroidManifest.xml文件内容复制进来
        File manifest = new File(f_mani);
        File manifest_bak = new File(f_mani_bak);
        // 拷贝文件 -- 此方法慎用，详见http://xiaoych.iteye.com/blog/149328
//        manifest.renameTo(manifest_bak);
//
//        for (int i = 0; i < 10; i++) {  //当文件还没有创建成功的时候暂停等待
//            if (manifest_bak.exists()) {
//                break;
//            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        try {
            ChannelHelper.copyFile(f_mani, f_mani_bak);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (manifest_bak.exists()) {
            System.out.println("==INFO 3.==移动文件成功======");
        } else {
            System.out.println("==ERROR==移动文件失败======");
        }

        // 创建生成结果的目录
        File f = new File("apk");
        if (!f.exists()) {
            f.mkdir();
        }

        /*
         * 遍历map，复制manifese进来，修改后打包，签名，存储在对应文件夹中
         */
        System.out.println("==INFO 4.1. == 正在生成包: " + channelName
                + " ======");
        BufferedReader br = null;
        FileReader fr = null;
        FileWriter fw = null;
        try {
            fr = new FileReader(manifest_bak);
            br = new BufferedReader(fr);
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {  //修改AndroidManifest.xml的meta-data字段
                if (line.contains(channel)) {
                    line = line.replaceAll(channel, channelValue);
                    System.out.println("替换为渠道号" + channelValue + "成功");
                }
                sb.append(line + "\n");
            }

            // 写回文件
            fw = new FileWriter(f_mani);
            fw.write(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
                if (br != null) {
                    br.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("==INFO 4.2. == 准备打包: " + channelName
                + " ======");

        // 打包 - 生成未签名的包
        String unsignApk = curPath + apkName.substring(Math.max(apkName.lastIndexOf("/"), apkName.lastIndexOf("\\") + 1)).replace(".apk", "") + "_apktool_" + channelName + "_un.apk";
        String cmdPack = String.format(
                "cmd.exe /C java -jar " + apktools + " b %s -o %s", dir, unsignApk);
        runtimeExec(cmdPack, new File(curPath));

        System.out.println("==INFO 4.3. == 开始签名: " + cmdPack
                + " ======");
        // 签名
        String signApk = curPath + apkName.substring(Math.max(apkName.lastIndexOf("/"), apkName.lastIndexOf("\\") + 1)).replace(".apk", "") + "_apktool_" + channelName + ".apk";
        String cmdKey = String
                .format("cmd.exe /C jarsigner -digestalg SHA1 -sigalg MD5withRSA -tsa https://timestamp.geotrust.com/tsa -verbose -keystore %s -signedjar %s %s %s -storepass  %s",
                        keyFile, signApk, unsignApk, keyFile, keyPasswd);

        String cmdKey1 = String
                .format("cmd.exe /C jarsigner  -verbose -sigalg SHA1withRSA -digestalg SHA1 -tsa https://timestamp.geotrust.com/tsa -keystore %s -storepass %s -signedjar %s %s %s", keyFile, keyPasswd, signApk, unsignApk, alias);
        runtimeExec(cmdKey1, new File(curPath));
        System.out.println("==INFO 4.4. == 签名成功: " + cmdKey1
                + " ======");
        // 删除未签名的包
        File unApk = new File(unsignApk);
        unApk.delete();

        //删除中途文件
        String cmdKeyy = String.format("cmd.exe /C rmdir /s/q %s", dir);
        runtimeExec(cmdKeyy, new File(curPath));
        manifest_bak.delete();

        System.out.println("==INFO 5 == 完成 ======");
    }

    /**
     * 执行指令 cmd指令
     *
     * @param cmd 指令内容
     */
    public void runCmd(String cmd) {
        Runtime rt = Runtime.getRuntime();
        BufferedReader br = null;
        InputStreamReader isr = null;
        try {
            Process p = rt.exec(cmd);
            // p.waitFor();    
            isr = new InputStreamReader(p.getInputStream());
            br = new BufferedReader(isr);
            String msg = null;
            while ((msg = br.readLine()) != null) {
                System.out.println(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("执行cmd命令出错");
        } finally {
            try {
                if (isr != null) {
                    isr.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean runtimeExec(String cmd, File path) {
        Process proc;
        try {
            Runtime rt = Runtime.getRuntime();
            proc = rt.exec(cmd, null, path);
            InputStream stderr = proc.getInputStream();

            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            int exitVal = proc.waitFor();
            return exitVal == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}