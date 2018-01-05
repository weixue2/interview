package com.huatu.tiku.interview.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author zhouwei
 * @Description: TODO
 * @create 2018-01-05 下午4:24
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="t_user")
@Entity
public class User {
    @Id
    private long id;
    /* 微信Id */
    private String openId;
    /* 姓名 */
    private String name;
    /* 手机号 */
    private String phone;
    /* 身份证 */
    private String idCard;
    /* 名族 */
    private String nation;
    /* 饮食禁忌 */
    private String diet;
    /* 生活禁忌 */
    private String life;
    /* 紧急联系人 */
    private String keyContact;
    /* 备考状态 */
    private int examStatus;
    /* 状态 */
    private int status;


}
