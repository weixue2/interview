package com.huatu.tiku.interview.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by x6 on 2018/1/25.
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="t_user_class")
public class UserClassRelation   extends BaseEntity  implements Serializable {

    /*
       status   1绑定中   -1 解除绑定关系
     */
//    //用户id（PhpUserId）
//    private long userId;
//
//    //用户id（PhpUserId）
//    private long phpUserId;

    //用户openId
    private String openId;


    //班级id
    private long classId;

    //绑定时间（精确到天）
    private String boundDate;
    
    
    



}
