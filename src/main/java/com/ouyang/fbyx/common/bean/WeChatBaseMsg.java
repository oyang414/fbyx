package com.ouyang.fbyx.common.bean;


import java.io.Serializable;

public class WeChatBaseMsg implements Serializable{
	private static final long serialVersionUID = 1306290773068167675L;

	/**
	 * 开发者微信号
	 */
	private String toUserName;
	
	/**
	 * 发送方账号(openId)
	 */
	private String fromUserName;
	
	/**
	 * 消息创建时间
	 */
	private Long createTime;
	
	/**
	 * 消息类型
	 * 1：text 文本消息
     * 2：image 图片消息
     * 3：voice 语音消息
     * 4：video 视频消息
     * 5：location 地址位置消息
     * 6：link 链接消息
     * 7：event 事件
	 */
	private String msgType;

	public WeChatBaseMsg(){
	}

	public WeChatBaseMsg(String toUserName, String fromUserName, Long createTime, String msgType) {
		super();
		this.toUserName = toUserName;
		this.fromUserName = fromUserName;
		this.createTime = createTime;
		this.msgType = msgType;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
}