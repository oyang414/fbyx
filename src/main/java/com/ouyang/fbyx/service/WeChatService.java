package com.ouyang.fbyx.service;

import com.ouyang.fbyx.common.bean.WeChatMsg;

/**
 * @description: 微信公众号Service
 * @author: ouyangxingjie
 * @create: 2021/7/19 20:06
 **/
public interface WeChatService {

    public WeChatMsg query(WeChatMsg weChatMsg);
}
