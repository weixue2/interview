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
    FIND_FAIL(50004,"查询数据错误"),

    IMAGE_FORMAT_ERROR(50010,"图片格式错误"),
    FILE_ERROR(50011,"文件读写错误"),
    UPLOAD_FILE_FAILED(50012,"上传文件失败"),

    PHP_ERROR(50020,"未知错误"),
    PHP_PARAMETER_ERROR(50021,"参数错误"),
    PHP_MOBILE_ERROR(50022,"手机号错误"),
    PHP_REQUEST_LOSED(50023,"请求失效"),

    CAPTCHA_EXPIRE(50030,"验证码过期"),
    CAPTCHA_ERROR(50031,"验证码错误"),
    CAPTCHA_PASS(20000,"验证通过"),

    PARAMETER_NULL_ERROR(50040,"验证通过"),

    OPENID_ERROR(50041,"OpenID错误"),

    NOTIFICATION_TYPE_ERROR(50042,"推送通知类型错误"),
    PUSH_TIME_ERROR(50043,"推送通知类型错误"),

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
