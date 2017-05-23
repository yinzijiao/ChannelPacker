package com.packer.model;

import java.util.List;

/**
 * Created by yin on 2017/5/23.
 */
public class YDResultBean {

    List<String> translation;
    String query;
    int errorCode;

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * 0 - 正常
     　20 - 要翻译的文本过长
     　30 - 无法进行有效的翻译
     　40 - 不支持的语言类型
     　50 - 无效的key
     　60 - 无词典结果，仅在获取词典结果生效
     * @return
     */
    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
