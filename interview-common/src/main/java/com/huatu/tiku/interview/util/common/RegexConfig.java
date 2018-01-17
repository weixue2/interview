package com.huatu.tiku.interview.util.common;

/**
 * 常用正则表达式
 * Created by linkang on 7/14/16.
 */
public class RegexConfig {

    /**
     *手机号
     */
    public final static String MOBILE_PHONE_REGEX = "(^(13\\d|14[57]|15[^4,\\D]|17[135678]|18\\d)\\d{8}|170[^346,\\D]\\d{7})";


    /**
     *邮箱
     */
    public final static String EMIAL_REGEX = "^([a-zA-Z0-9_-]+)@(([a-zA-Z0-9_-]+)(\\.[a-zA-Z0-9_-]+)+)";

    /**
     *昵称正则表达式
     * 长度为2-12，数字，中英文及下划线
     */
    public final static String NICK_NAME_REGEX = "[a-zA-Z_0-9\\u4e00-\\u9fa5]{2,12}";


    /**
     * 收货人名字
     * 只允许中英文,长度为2-15
     */
    public final static String CONSIGNEE_NAME_REGEX = "[a-zA-Z\\u4e00-\\u9fa5]{2,15}";
}
