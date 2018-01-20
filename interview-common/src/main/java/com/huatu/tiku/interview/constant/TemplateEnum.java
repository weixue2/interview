package com.huatu.tiku.interview.constant;

import com.huatu.tiku.interview.entity.template.MyTreeMap;
import com.huatu.tiku.interview.entity.template.TemplateMap;
import com.huatu.tiku.interview.entity.template.WechatTemplateMsg;
import java.util.TreeMap;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/12 20:21
 * @Description 这里存放一些固定的模板消息,也可以存放暂不设置参数内容的模板，只需要null就行了。。
 */

public enum TemplateEnum {
    No_0("templateId","description","url",null),

    No_1(   "5BP20zR4h-LaEfFWbiqOrw4CPXEqfCxi4v5kNBXHqAc",
            "没有描述",
            "http://music.163.com/song?id=498040743&userid=84550482",
            MyTreeMap.createMap(
                    new TemplateMap("first", WechatTemplateMsg.item("sfdg","")),
                    new TemplateMap("first", WechatTemplateMsg.item("asdf",""))
            )
    ),
    No_2("z5nKybDUuLUxbUJR6C1QleQsFdhjs_iBQey0OcJvlRw",
            "还是没有描述",
            "http://music.163.com/song?id=498040743&userid=84550482",
            MyTreeMap.createMap(
                    new TemplateMap("first", WechatTemplateMsg.item("first111","#000000")),
                    new TemplateMap("keyword1", WechatTemplateMsg.item("keyword12222","#000000")),
                    new TemplateMap("keyword2", WechatTemplateMsg.item("keyword22222","#000000")),
                    new TemplateMap("keyword3", WechatTemplateMsg.item("keyword32222","#000000")),
                    new TemplateMap("keyword4", WechatTemplateMsg.item("keyword42222","#000000"))
                    )
    ),
    No_3("","","",null)
    ,
    MorningReading(
//            "Cpj93XZXzwagt_bp2QSW9iYVRkF9FCaRtHtXnoLzSHU",
            "dqB6dsTBx_FzoeMtflHHJwDAZdL1X2R_4o3eu4290s4", //我的
            "鸡汤晨读",
            BasicParameters.MorningReadingURL,
            null),

    /*每日学习统计*/
    DailyReport("krkjVlxdeNqhIsd2VH_4FukKinQAJXs3cqiR9XsAFPs","每日学习统计","",MyTreeMap.createMap(
            new TemplateMap("first", WechatTemplateMsg.item("今日学习报告已经生成","#000000")),
            new TemplateMap("remark", WechatTemplateMsg.item("好好学习，天天向上","#000000"))
//            new TemplateMap("keyword1", WechatTemplateMsg.item("keyword12222","#000000")),
//            new TemplateMap("keyword2", WechatTemplateMsg.item("keyword22222","#000000")),
//            new TemplateMap("keyword3", WechatTemplateMsg.item("keyword32222","#000000")),
//            new TemplateMap("keyword4", WechatTemplateMsg.item("keyword42222","#000000"))
    )),

    /*线下学习统计*/
    TotalReport("Bk7qZ79sMZoMivyBlDbJwnN4o0oxGJ3AwiN0YCfSK20","线下学习历程","",MyTreeMap.createMap(
            new TemplateMap("first", WechatTemplateMsg.item("线下学习历程已经生成","#000000")),
            new TemplateMap("remark", WechatTemplateMsg.item("好好学习，天天向上","#000000"))
//            new TemplateMap("keyword1", WechatTemplateMsg.item("keyword12222","#000000")),
//            new TemplateMap("keyword2", WechatTemplateMsg.item("keyword22222","#000000")),
//            new TemplateMap("keyword3", WechatTemplateMsg.item("keyword32222","#000000")),
//            new TemplateMap("keyword4", WechatTemplateMsg.item("keyword42222","#000000"))
    )),
    ;

    TemplateEnum(String templateId, String description,String url,TreeMap<String, TreeMap<String,String>> data) {
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

    public TreeMap<String, TreeMap<String, String>> getData() {
        return data;
    }

    private String templateId;
    private String description;
    private String url;
    private TreeMap<String, TreeMap<String,String>> data;
    // 还有一些参数吧。。


}
