package com.huatu.tiku.interview.constant;

/**
 * Created by x6 on 2018/1/19.
 */
public enum NotificationTypeConstant {
    //    通知类型  1线上课程安排  2晨读鸡汤  3 报道通知
    ONLINE_COURSE_ARRANGEMENT(1),
    MORNING_READING(2),
    REGISTER_REPORT(3);


    NotificationTypeConstant(int code) {
        this.code = code;
    }
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
