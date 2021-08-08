package com.ouyang.fbyx.common.utils;

import com.ouyang.fbyx.common.bean.WeChatMsg;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 解析xml
 * @author: ouyangxingjie
 * @create: 2021/7/19 21:28
 **/
public class WeChatMsgParseUtil {

    private WeChatMsgParseUtil(){}
    public static Map<String,String> parseXml(HttpServletRequest request){

        Map<String,String> messageMap=new HashMap<>();
        InputStream inputStream=null;
        try {
            //读取request Stream信息
            inputStream=request.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SAXReader reader = new SAXReader();
        Document document=null;
        try {
            document = reader.read(inputStream);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Element root=document.getRootElement();
        List<Element> elementsList=root.elements();

        for(Element e:elementsList){
            messageMap.put(e.getName(),e.getText());
        }
        try {
            inputStream.close();
            inputStream=null;
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return messageMap;
    }

    public static String toXml(WeChatMsg weChatMsg){
        String result = "<xml>" +
                "<ToUserName><![CDATA[" + weChatMsg.getFromUserName() + "]]></ToUserName>" +
                "<FromUserName><![CDATA[" + weChatMsg.getToUserName()+ "]]></FromUserName>" +
                "<CreateTime>" + weChatMsg.getCreateTime() + "</CreateTime>"+
                "<MsgType><![CDATA["+ weChatMsg.getMsgType() + "]]></MsgType>" +
                "<Content><![CDATA["+ weChatMsg.getContent() +"]]></Content>" +
                "</xml>";
        return result;
    }
}
