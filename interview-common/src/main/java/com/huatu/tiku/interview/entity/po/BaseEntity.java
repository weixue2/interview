package com.huatu.tiku.interview.entity.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@MappedSuperclass
@DynamicInsert
@DynamicUpdate(true)
public class BaseEntity {
    @Id
    @GeneratedValue
    protected long id;
    @Column(columnDefinition = "smallint default 0")
    protected int bizStatus;
    @Column(columnDefinition = "smallint default 1")
    protected int status;
    @Column(columnDefinition = "varchar(128) default 'admin'")
    protected String creator;
    @Column(columnDefinition = "varchar(128) default 'admin'")
    protected String modifier;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    protected Date gmtCreate;
    @org.hibernate.annotations.UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    protected Date gmtModify;
}
