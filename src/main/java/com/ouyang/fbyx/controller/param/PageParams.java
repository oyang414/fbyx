package com.ouyang.fbyx.controller.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * @description: 分页查询参数
 * @author: ouyangxingjie
 * @create: 2021/7/7 21:07
 **/
@Data
@ApiModel(value = "PageParams",description = "分页查询参数")
public class PageParams implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("page_num")
    @Max(value = Integer.MAX_VALUE,message = "当前页码超过最大长度！")
    @Min(value = 1,message = "当前页码最小不能小于1")
    @ApiModelProperty(name = "page_num",value = "当前页码",example = "1")
    @ApiParam(name = "page_num", value = "当前页码",example = "1")
    private Integer pageNum;

    @JsonProperty("page_size")
    @Max(value = Integer.MAX_VALUE,message = "每页条数超过最大长度！")
    @Range(min = 5, max = 100,message = "范围应在5-100之间")
    @ApiModelProperty(name ="page_size",value = "每页条数",example = "5")
    @ApiParam(name = "page_size", value = "每页条数",example = "5")
    private Integer pageSize;


}
