package com.huatu.tiku.interview.entity.result;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huatu.tiku.interview.constant.ResultEnum;

import java.util.List;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/12 9:12
 * @Modefied By:
 */
public class ReqResult implements Result{
    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // 响应业务状态
    private Integer code;

    // 响应消息
    private String message;

    // 响应中的数据
    private Object data;

    public static ReqResult build(Integer code, String message, Object data) {
        return new ReqResult(code, message, data);
    }

    public static ReqResult ok(Object data) {
        return new ReqResult(data);
    }

    public static ReqResult ok() {
        return new ReqResult(null);
    }

    public ReqResult() {

    }

    public static ReqResult build(Integer code, String message) {
        return new ReqResult(code, message, null);
    }

    public static ReqResult build(ResultEnum resultEnum) {
        return new ReqResult(resultEnum.getCode(), resultEnum.getMessage(), null);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ReqResult(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ReqResult(Object data) {
        this.code = ResultEnum.success.getCode();
        this.message = ResultEnum.success.getMessage();
        this.data = data;
    }

//    public Boolean isOK() {
//        return this.code == 200;
//    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }



    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 将json结果集转化为Result对象
     *
     * @param jsonData json数据
     * @param clazz Result中的object类型
     * @return
     */
    public static ReqResult formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, ReqResult.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (clazz != null) {
                if (data.isObject()) {
                    obj = MAPPER.readValue(data.traverse(), clazz);
                } else if (data.isTextual()) {
                    obj = MAPPER.readValue(data.asText(), clazz);
                }
            }
            return build(jsonNode.get("code").intValue(), jsonNode.get("message").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 没有object对象的转化
     *
     * @param json
     * @return
     */
    public static ReqResult format(String json) {
        try {
            return MAPPER.readValue(json, ReqResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Object是集合转化
     *
     * @param jsonData json数据
     * @param clazz 集合中的类型
     * @return
     */
    public static ReqResult formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("code").intValue(), jsonNode.get("message").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }
}
