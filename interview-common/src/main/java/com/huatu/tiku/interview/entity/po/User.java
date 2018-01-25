package com.huatu.tiku.interview.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author zhouwei
 * @Description: 学员信息表
 * @create 2018-01-05 下午4:24
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="t_user")
@Entity
public class User {

    @Id @GeneratedValue
    private long id;

    private long phpUserId;
    @Column(columnDefinition="varchar(50) COMMENT '微信Id啊'")
    private String openId;
    @Column(columnDefinition="varchar(20) COMMENT '姓名'")
    private String name;
    @Column(columnDefinition="varchar(15) COMMENT '手机号'")
    private String phone;
    @Column(columnDefinition="varchar(20) COMMENT '身份证'")
    private String idCard;
    @Column(columnDefinition="varchar(15) COMMENT '民族呗'")
    private String nation;

    private Integer sex;

    private Boolean pregnancy;
//    @Column(columnDefinition="varchar(255) COMMENT '饮食禁忌'")
//    private String diet;
//    @Column(columnDefinition="varchar(255) COMMENT '生活禁忌'")
//    private String life;
    @Column(columnDefinition="varchar(50) COMMENT '紧急联系人'")
    private String keyContact;
//    @Column(columnDefinition="int(2) COMMENT '备考状态'")
//    private int examStatus;
//    @Column(columnDefinition="int(2) COMMENT '状态'")
    private Integer status;

    private Boolean agreement;
}
