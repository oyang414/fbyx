package com.ouyang.fbyx.controller.param;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * @description: 花名册传入参数
 * @author: ouyangxingjie
 * @create: 2021/6/28 18:45
 **/
@Data
@ApiModel(value = "RosterParams",description = "花名册传入参数")
public class RosterParams implements Serializable{

    private static final long serialVersionUID = 1L;

    //玩家名称
    @JsonProperty("user_name")
    @NotBlank(message = "玩家名称不能为空！")
    @Length(max = 20,message = "长度超过20！")
    @ApiModelProperty(name = "user_name", value = "玩家名称",example = "Misaka",required = true)
    @ApiParam(name = "user_name", value = "玩家名称",example = "Misaka",required = true)
    private String userName;
    //玩家唯一标识
    @JsonProperty("user_id")
    @Max(value = Integer.MAX_VALUE,message = "玩家唯一标识超过最大长度！")
    @ApiModelProperty(name = "user_id", value = "玩家唯一标识",example = "4396")
    @ApiParam(name = "user_id", value = "玩家唯一标识",example = "4396")
    private Integer userId;
    //英雄名称
    @JsonProperty("hero_name")
    @NotBlank(message = "英雄名称不能为空！")
    @Length(max = 20,message = "长度超过20！")
    @ApiModelProperty(name = "hero_name",value = "英雄名称",example = "屠夫",required = true)
    @ApiParam(name = "hero_name", value = "英雄名称",example = "屠夫",required = true)
    private String heroName;
    //原因
    @Length(max = 500,message = "长度超过500字！")
    @ApiModelProperty(name = "reason",value = "原因",example = "挂机")
    @ApiParam(name = "reason", value = "原因",example = "挂机")
    private String reason;
    //玩家喷人话语
    @Length(max = 500,message = "长度超过500字！")
    @ApiModelProperty(name = "statement",value = "玩家喷人语句",example = "三个打一个还被反杀？会不会玩！？")
    @ApiParam(name = "statement", value = "玩家喷人语句",example = "三个打一个还被反杀？会不会玩！？")
    private String statement;
}
