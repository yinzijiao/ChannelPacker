#Android studio 打包插件

    1、直接写入META-INF文件夹内，不需要重新打包(需要在build.gradle中设置 : v2SigningEnabled false)
    2、用apktool工具反编译apk，替换AndroidManifest.xml中对应的文本

# 渠道文件格式
    
    CHANNEL 应用汇 yingyonghui
    CHANNEL 豌豆荚 wandoujia
    
##第一种方式的渠道获取方法

    public static String getChannel(Context context) {
        if (channel != null) {
            return channel;
        }
        final String start = "META-INF/channel_";
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.contains(start)) {
                    channel = entryName.replace(start, "");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (channel == null || channel.length() <= 0) {
            channel = "default";//默认渠道号
        }
        return channel;
    }
    
    
    友盟设置渠道号
    UMAnalyticsConfig(Context context, String appkey, String channelId)
   
    UMAnalyticsConfig(Context context, String appkey, String channelId, EScenarioType eType)
    
    UMAnalyticsConfig(Context context, String appkey, String channelId, EScenarioType eType,Boolean isCrashEnable)
    MobclickAgent. startWithConfigure(UMAnalyticsConfig config)