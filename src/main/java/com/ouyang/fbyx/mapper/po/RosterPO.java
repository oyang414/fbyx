package com.ouyang.fbyx.mapper.po;

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
@TableName("tb_sb_roster")
public class RosterPO {
    //主键
    @TableId(type = IdType.AUTO)
    private int id ;
    //玩家名称
    @TableField("user_name")
    private String userName;
    //玩家唯一标识
    @TableField("user_id")
    private int userId;
    //英雄名称
    @TableField("hero_name")
    private String heroName;
    //原因
    private String reason;
    //玩家喷人话语
    private String statement;
    //创建时间
    @TableField("create_time")
    private LocalDateTime createTime;


}
