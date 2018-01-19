package com.huatu.tiku.interview.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/18 13:04
 * @Description
 */
@Data
public class ReadingTemp implements Serializable {
    private Long id;
    private Date date;
    private Integer type;
    private Boolean status;

    public ReadingTemp(Long id, Date date, Boolean status,Integer type) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.type = type;
    }

    public ReadingTemp() {
    }
}
