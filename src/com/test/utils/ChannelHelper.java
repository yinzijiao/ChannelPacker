package com.test.utils;

import java.io.*;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yin on 2017/5/18.
 */
public class ChannelHelper {

    private static final String CHANNEL_PREFIX = "/META-INF/";
    private static final String CHANNEL_PATH_MATCHER = "regex:/META-INF/mtchannel_[0-9a-zA-Z]{1,5}";
    private static String source_path;
    private static final String channel_file_name = "channel_list.txt";
    private static final String channel_flag = "channel_";

    public static void main(String[] args) throws Exception {
        if (args.length <= 0) {
            System.out.println("请输入文件路径作为参数");
            return;
        }


        final String source_apk_path = args[0];//main方法传入的源apk的路径，是执行jar时命令行传入的，不懂的往下看。
        int last_index = source_apk_path.lastIndexOf("/") + 1;
        source_path = source_apk_path.substring(0, last_index);
        final String source_apk_name = source_apk_path.substring(last_index, source_apk_path.length());

        System.out.println("包路径：" + source_path);
        System.out.println("文件名：" + source_apk_name);

        ArrayList<String> channel_list = getChannelList(source_path + channel_file_name);
        final String last_name = ".apk";
        for (int i = 0; i < channel_list.size(); i++) {
            final String new_apk_path = source_path + source_apk_name.substring(0, source_apk_name.length() - last_name.length()) //
                    + "_" + channel_list.get(i) + last_name;
            copyFile(source_apk_path, new_apk_path);
            changeChannel(new_apk_path, channel_flag + channel_list.get(i));
        }
    }

    /**
     * 修改渠道号，原理是在apk的META-INF下新建一个文件名为渠道号的文件
     */
    public static boolean changeChannel(final String zipFilename, final String channel) {
        try (FileSystem zipfs = createZipFileSystem(zipFilename, false)) {

            final Path root = zipfs.getPath("/META-INF/");
            ChannelFileVisitor visitor = new ChannelFileVisitor();
            Files.walkFileTree(root, visitor);

            Path existChannel = visitor.getChannelFile();
            Path newChannel = zipfs.getPath(CHANNEL_PREFIX + channel);
            if (existChannel != null) {
                Files.move(existChannel, newChannel, StandardCopyOption.ATOMIC_MOVE);
            } else {
                Files.createFile(newChannel);
            }

            return true;

        } catch (IOException e) {
            System.out.println("添加渠道号失败：" + channel);
            e.printStackTrace();
        }

        return false;
    }

    private static FileSystem createZipFileSystem(String zipFilename, boolean create) throws IOException {
        final Path path = Paths.get(zipFilename);
        final URI uri = URI.create("jar:file:" + path.toUri().getPath());

        final Map<String, String> env = new HashMap<>();
        if (create) {
            env.put("create", "true");
        }
        return FileSystems.newFileSystem(uri, env);
    }

    private static class ChannelFileVisitor extends SimpleFileVisitor<Path> {
        private Path channelFile;
        private PathMatcher matcher = FileSystems.getDefault().getPathMatcher(CHANNEL_PATH_MATCHER);

        public Path getChannelFile() {
            return channelFile;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (matcher.matches(file)) {
                channelFile = file;
                return FileVisitResult.TERMINATE;
            } else {
                return FileVisitResult.CONTINUE;
            }
        }
    }

    /**
     * 得到渠道列表
     */
    private static ArrayList<String> getChannelList(String filePath) {
        ArrayList<String> channel_list = new ArrayList<String>();

        try {
            String encoding = "UTF-8";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    // System.out.println(lineTxt);
                    if (lineTxt != null && lineTxt.length() > 0) {
                        channel_list.add(lineTxt);
                    }
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

        return channel_list;
    }

    /**
     * 复制文件
     */
    public static void copyFile(final String source_file_path, final String target_file_path) throws IOException {

        File sourceFile = new File(source_file_path);
        File targetFile = new File(target_file_path);

        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } catch (Exception e) {
            System.out.println("复制文件失败：" + target_file_path);
            e.printStackTrace();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

//    public static String getChannel(Context context) {
//        if (channel != null) {
//            return channel;
//        }
//
//        final String start_flag = "META-INF/channel_";
//        ApplicationInfo appinfo = context.getApplicationInfo();
//        String sourceDir = appinfo.sourceDir;
//        ZipFile zipfile = null;
//        try {
//            zipfile = new ZipFile(sourceDir);
//            Enumeration<?> entries = zipfile.entries();
//            while (entries.hasMoreElements()) {
//                ZipEntry entry = ((ZipEntry) entries.nextElement());
//                String entryName = entry.getName();
//                if (entryName.contains(start_flag)) {
//                    channel = entryName.replace(start_flag, "");
//                    break;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (zipfile != null) {
//                try {
//                    zipfile.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        if (channel == null || channel.length() <= 0) {
//            channel = "guanwang";//读不到渠道号就默认是官方渠道
//        }
//        return channel;
//    }
}
