package com.itheima.reggie.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Transcation implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String tableName;

    private String originValue;

    private String newValue;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

}
