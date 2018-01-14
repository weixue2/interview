package com.huatu.tiku.interview.constant;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/11 15:05
 * @Modefied By:
 */
public enum ResultEnum {
    success(20000,"成功"),
    error(50000,"错误"),
    insertFail(50001,"插入数据失败"),
    imageFormatError(50002,"图片格式错误"),
    fileError(50003,"文件读写错误"),
    delFail(50004,"删除数据错误"),
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
