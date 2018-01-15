package com.huatu.tiku.interview.constant;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/11 15:05
 * @Modefied By: 返回码啥的
 */
public enum ResultEnum {
    SUCCESS(20000,"成功"),
    ERROR(50000,"错误"),

    INSERT_FAIL(50001,"插入数据失败"),
    DELETE_FAIL(50002,"删除数据错误"),
    UPDATE_FAIL(50003,"更新数据错误"),

    IMAGE_FORMAT_ERROR(50010,"图片格式错误"),
    FILE_ERROR(50011,"文件读写错误"),

    UPLOAD_FILE_FAILED(50012,"上传文件失败"),
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
