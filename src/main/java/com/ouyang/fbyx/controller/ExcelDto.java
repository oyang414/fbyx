package com.ouyang.fbyx.controller;

import cn.hutool.core.date.DateTime;
import lombok.Data;

/**
 * @description:
 * @author: ouyangxingjie
 * @create: 2021/11/24 19:04
 **/
@Data
public class ExcelDto {

    private String userName;

    private String contractNo;

    //private DateTime signDate;

    private DateTime creditDate;

    private String location;

    private String distance;

    private String area;



}
