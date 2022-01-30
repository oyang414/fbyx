package com.ouyang.fbyx.test;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 花名册Controller
 * @author: ouyangxingjie
 * @create: 2021/6/27 14:12
 **/
@RestController
@RequestMapping(value = "/test",produces="application/json")
@Validated
@Api(tags = "风暴英雄傻逼花名册相关接口")
@Slf4j
public class TestController {

    @Autowired
    private TestService testService;


    @RequestMapping(method = RequestMethod.GET)
    public void test(){
        testService.addGoods();
        //testService.addImage();
    }


}
