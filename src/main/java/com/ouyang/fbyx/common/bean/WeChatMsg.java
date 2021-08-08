package com.ouyang.fbyx.common.bean;

import lombok.Data;

/**
 * @description:
 * @author: ouyangxingjie
 * @create: 2021/7/19 21:52
 **/
@Data
public class WeChatMsg extends WeChatBaseMsg {

    private String content;

    private String event;


}
