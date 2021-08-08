package com.ouyang.fbyx.common.bean;

/**
 * @description: 微信事件类型枚举
 * @author: ouyangxingjie
 * @create: 2021/7/19 21:36
 **/
public enum WeChatEventType {

    SUBSCRIBE(1, "subscribe"),

    UNSUBSCRIBE(2, "unsubscribe");
    private int type;
    private String name;

    WeChatEventType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getNameByType(int type) {
        for (WeChatEventType weChatMsgType : WeChatEventType.values()) {
            if (weChatMsgType.getType() == type) {
                return weChatMsgType.name;
            }
        }
        return "";
    }

    public static Integer getTypeByName(String name) {
        for (WeChatEventType weChatMsgType : WeChatEventType.values()) {
            if (weChatMsgType.getName().equalsIgnoreCase(name)) {
                return weChatMsgType.type;
            }
        }
        return null;
    }



}
