package com.huatu.tiku.interview.constant;

import com.huatu.tiku.interview.entity.template.TemplateData;
import lombok.Data;

import java.util.Map;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/12 20:21
 * @Description
 */

public enum TemplateEnum {
    No_1(   "5BP20zR4h-LaEfFWbiqOrw4CPXEqfCxi4v5kNBXHqAc",
            "没有描述",
            "http://music.163.com/song?id=498040743&userid=84550482",
            null),
    ;

    TemplateEnum(String templateId, String description,String url,Map<String, TemplateData> data) {
        this.templateId = templateId;
        this.description = description;
        this.url = url;
        this.data = data;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, TemplateData> getData() {
        return data;
    }

    private String templateId;
    private String description;
    private String url;
    private Map<String, TemplateData> data;
    // 还有一些参数吧。。
}
