package com.huatu.tiku.interview.constant;

/**
 * @author zhouwei
 * @Description: TODO
 * @create 2018-01-03 下午7:48
 **/
public class WeChatUrlConstant {
    /* 获取用户信息  phone=xxxxx&timeStamp=xxxxxx进行3des加密传参，返回值也是3des加密的   */
    public static String PHP_GET_USER_INFO = "http://testapi.huatu.com/app_ztk/getGdmsinfo.php?p=";

    public static String ACCESS_TOKEN = "ACCESS_TOKEN";
    /*发送模板消息接口*/
    public static String TEMPLATE_NEWS_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
    /*  秘钥  */
   // public static final String ACCESS_SECRETE = "78b688afb80faf735ebf3e2a3525ef35";
    //public static final String APP_ID = "wx4e6e2dc624081b58";
    public static String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=78b688afb80faf735ebf3e2a3525ef35&secret=78b688afb80faf735ebf3e2a3525ef35";
    // 菜单创建（POST） 限1000（次/天）
    public static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    // 菜单查询（POST） 限10000（次/天）
    public static String MENU_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";

    // 菜单删除（POST） 限1000（次/天）
    public static String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";


    public static String MENU_CONTENT = "{\"button\":[{\"name\":\"休闲娱乐\",\"sub_button\":[{\"type\":\"view\",\"name\":\"韩晶晶\",\"key\":\"m_joke\",\"url\":\"http://tkproc.huatu.com/shenlun-backend/#/home/danti_list\"},{\"type\":\"click\",\"name\":\"内涵段子\",\"key\":\"m_duanzi\"},{\"type\":\"click\",\"name\":\"爆笑图片\",\"key\":\"m_laughImg\"}]},{\"name\":\"实用工具\",\"sub_button\":[{\"type\":\"click\",\"name\":\"天气查询\",\"key\":\"m_weather\"},{\"type\":\"click\",\"name\":\"公交查询\",\"key\":\"m_bus\"},{\"type\":\"click\",\"name\":\"功能菜单\",\"key\":\"m_sysmenu\"}]},{\"name\":\"消息示例\",\"sub_button\":[{\"type\":\"click\",\"name\":\"关于企特\",\"key\":\"m_about\"},{\"type\":\"click\",\"name\":\"图文消息\",\"key\":\"m_imgmsg\"},{\"type\":\"click\",\"name\":\"音乐消息\",\"key\":\"m_musicmsg\"}]}]}";
}
