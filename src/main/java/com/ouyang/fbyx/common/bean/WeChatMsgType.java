package com.ouyang.fbyx.common.bean;

/**
 * @description: 微信消息类型枚举
 * @author: ouyangxingjie
 * @create: 2021/7/19 21:36
 **/
public enum WeChatMsgType {

    TEXT(1, "text"),

    IMAGE(2, "image"),

    VOICE(3, "voice"),

    VIDEO(4,"video"),

    LOCATION(5,"location"),

    LINK(6,"link"),

    EVENT(7,"event"),

    MUSIC(8,"music"),

    NEWS(9,"news");
    private int type;
    private String name;

    WeChatMsgType(int type, String name) {
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
        for (WeChatMsgType weChatMsgType : WeChatMsgType.values()) {
            if (weChatMsgType.getType() == type) {
                return weChatMsgType.name;
            }
        }
        return "";
    }

    public static Integer getTypeByName(String name) {
        for (WeChatMsgType weChatMsgType : WeChatMsgType.values()) {
            if (weChatMsgType.getName().equalsIgnoreCase(name)) {
                return weChatMsgType.type;
            }
        }
        return null;
    }



}
