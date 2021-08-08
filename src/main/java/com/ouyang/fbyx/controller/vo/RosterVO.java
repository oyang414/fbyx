package com.ouyang.fbyx.controller.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 花名册VO
 * @author: ouyangxingjie
 * @create: 2021/6/27 14:10
 **/
@Data
public class RosterVO implements Serializable {

    private static final long serialVersionUID = 1L;

    //玩家名称
    @JsonProperty("user_name")
    private String userName;
    //英雄名称
    @JsonProperty("hero_name")
    private String heroName;
    //玩家唯一标识
    @JsonProperty("user_id")
    private int userId;
    //原因
    private String reason;
    //玩家喷人话语
    private String statement;
    //创建时间
    @JsonProperty("create_time")
    private String createTime;
}
