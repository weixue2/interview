package com.huatu.tiku.interview.constant;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/11 15:05
 * @Modefied By:
 */
public enum ResultEnum {
    success(20000,"成功"),
    error(50000,"错误"),
    insertFail(50001,"插入失败"),

    ;
    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
