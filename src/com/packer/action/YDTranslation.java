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
import com.packer.utils.UIHelper;

/**
 * Created by yin on 2017/5/23.
 */
public class YDTranslation extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = e.getData(LangDataKeys.EDITOR);
        String text = editor.getSelectionModel().getSelectedText();
        if (!StringUtil.isEmpty(text)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = HttpRequest.sendGet(Constants.YD_BASE_URL, String.format(Constants.YD_URL_PARAM, text));
                    Logger.e(result);
                    YDResultBean resultBean = new Gson().fromJson(result, YDResultBean.class);
                    if (resultBean != null) {
                        String showText = "";
                        switch (resultBean.getErrorCode()) {
                            case 0:
                                StringBuilder builder = new StringBuilder();
                                for (int i = 0; i < resultBean.getTranslation().size(); i++) {
                                    builder.append(i + 1 + "、" + resultBean.getTranslation().get(i) + "\n");
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
                    }
                }
            }).start();
        }
    }
}
