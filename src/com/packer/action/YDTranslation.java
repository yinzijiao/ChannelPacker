package com.packer.action;

import com.google.gson.Gson;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.text.StringUtil;
import com.packer.Constants;
import com.packer.model.YDResultBean;
import com.packer.utils.HttpRequest;
import com.packer.utils.Logger;
import com.packer.utils.ResUtil;
import com.packer.utils.UIHelper;

/**
 * Created by yin on 2017/5/23.
 */
public class YDTranslation extends AnAction {

    private Editor editor;
    private int repeatCount = 0;

    @Override
    public void actionPerformed(AnActionEvent e) {
        editor = e.getData(LangDataKeys.EDITOR);
        String text = editor.getSelectionModel().getSelectedText();
        if (!StringUtil.isEmpty(text)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    translation(text);
                }
            }).start();
        }
    }

    private void translation(String text) {
        String result = null;
        try {
            result = HttpRequest.sendGet(Constants.YD_BASE_URL, String.format(Constants.YD_URL_PARAM, ResUtil.getUrlEncode(text)));
        } catch (Exception e1) {
            result = HttpRequest.sendGet(Constants.YD_BASE_URL, String.format(Constants.YD_URL_PARAM, text));
        }
        Logger.e(result);
        YDResultBean resultBean = new Gson().fromJson(result, YDResultBean.class);
        if (resultBean != null) {
            String showText = "";
            switch (resultBean.getErrorCode()) {
                case 0:
                    if (repeatCount < 10 && resultBean.getTranslation() != null && resultBean.getTranslation().size() > 0 && text.equals(resultBean.getTranslation().get(0))) {
                        translation(createCorrectText(text));
                        Logger.e(createCorrectText(text) + repeatCount);
                        repeatCount++;
                        return;
                    }
                    StringBuilder builder = new StringBuilder();
                    if (resultBean.getTranslation().size() > 1) {
                        for (int i = 0; i < resultBean.getTranslation().size(); i++) {
                            builder.append(i + 1 + "、" + resultBean.getTranslation().get(i) + "\n");
                        }
                    } else {
                        builder.append(resultBean.getTranslation().get(0) + "\n");
                    }
                    showText = builder.toString();
                    break;
                case 20:
                    showText = "错误：要翻译的文本过长";
                    break;
                case 30:
                    showText = "错误：无法进行有效的翻译";
                    break;
                case 40:
                    showText = "错误：不支持的语言类型";
                    break;
                case 50:
                    showText = "错误：无效的key";
                    break;
                case 60:
                    showText = "错误：无词典结果，仅在获取词典结果生效";
                    break;
                default:
                    break;
            }
            UIHelper.showPopupBalloon(editor, showText);
            repeatCount = 0;
        }
    }

    private String createCorrectText(String text) {
        String str = text;
        if (str.contains("_")) {
            str = str.replace("_", " ");
        }
        if (Math.abs(str.compareTo(str.toLowerCase())) > 0) {
            int iMax = str.length() + Math.abs(str.compareTo(str.toLowerCase())) / 32;
            for (int i = 0; i < iMax; i++) {
                if (str.charAt(i) != str.toLowerCase().charAt(i)) {
                    str = str.substring(0, i) + " " + str.substring(i);
                    i++;
                }
            }
        }
        return str;
    }
}
