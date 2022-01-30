package com.ouyang.fbyx.test;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description: 花名册PO
 * @author: ouyangxingjie
 * @create: 2021/6/27 13:50
 **/
@Data
@TableName("tb_image")
public class ImagePO {
    //主键
    @TableId(type = IdType.AUTO)
    private int id ;
    //玩家名称
    @TableField("name")
    private String name;


}
