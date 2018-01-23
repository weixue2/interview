package com.huatu.tiku.interview.entity.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author jbzm
 * @Date Create on 2018/1/23 10:23
 */
@Data
@Entity
@Table(name = "t_admin")
public class Admin extends BaseEntity {
    private String username;
    private String password;
}
