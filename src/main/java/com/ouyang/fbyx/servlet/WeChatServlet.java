package com.ouyang.fbyx.servlet;

import com.ouyang.fbyx.common.bean.WeChatEventType;
import com.ouyang.fbyx.common.bean.WeChatMsg;
import com.ouyang.fbyx.common.bean.WeChatMsgType;
import com.ouyang.fbyx.common.utils.BeanConverter;
import com.ouyang.fbyx.common.utils.SignUtil;
import com.ouyang.fbyx.common.utils.WeChatMsgParseUtil;
import com.ouyang.fbyx.service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

/**
 * @description: 微信Servlet
 * @author: ouyangxingjie
 * @create: 2021/7/1 21:26
 **/
@Slf4j
@WebServlet(name = "weChatServlet", urlPatterns = "/weChat")  //标记为servlet，以便启动器扫描。
public class WeChatServlet extends HttpServlet {

    @Autowired
    private WeChatService weChatService;

    private static final String KEYWORD = "sb";

    private static final String CN_KEYWORD = "我遇到伞兵了";

    private static final String FRONT_URL = "http://139.198.183.194:8022/";

    /**
     * 确认请求来自微信服务器
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("微信请求接入...");

        //获取随机字符串
        String echostr = req.getParameter("echostr");

        PrintWriter out = resp.getWriter();
        //判断加密后的字符串和签名是否一样.如果一样表示接入成功
        if (validate(req)) {
            log.info("微信公众号接入成功...");
            out.print(echostr);
        }
        out.close();
        out = null;
    }

    /**
     * @param req
     * @param resp
     * @description 来自微信公众号的请求
     * @return void
     * @author ouyangxingjie
     * @date 2021/7/19 22:42
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String respMessage = null;
        WeChatMsg weChatMsg = buildWeChatBaseMsg(req);
        if(weChatMsg.getMsgType().equals(WeChatMsgType.EVENT.getName())){
            if(weChatMsg.getEvent().equals(WeChatEventType.SUBSCRIBE.getName())){
                //如果是关注事件
                //文本消息
                WeChatMsg text = BeanConverter.convert(weChatMsg,WeChatMsg.class);
                text.setContent("怎么？遇到伞兵了？欢迎使用风暴英雄伞兵花名册！（输入：'sb'或者'我遇到伞兵了'即可使用录入伞兵功能）");
                text.setMsgType(WeChatMsgType.TEXT.getName());
                text.setCreateTime(new Date().getTime());
                respMessage = WeChatMsgParseUtil.toXml(text);
                log.info(respMessage);
            }
        }else if(weChatMsg.getMsgType().equals(WeChatMsgType.TEXT.getName())){
            // 文字消息，则做查询
            WeChatMsg result = weChatMsg;
            // 如果文字消息是关键字，则返回录入伞兵的网址
            if(KEYWORD.equalsIgnoreCase(weChatMsg.getContent()) || CN_KEYWORD.equals(weChatMsg.getContent())){
                result.setContent(FRONT_URL);
            }else {
                // 否则做玩家姓名的查询
                result = weChatService.query(weChatMsg);
            }
            result.setCreateTime(new Date().getTime());
            respMessage = WeChatMsgParseUtil.toXml(result);
            log.info(respMessage);
        }
        resp.setContentType("text/html;charset=UTF-8");
        // 响应消息
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            out.print(respMessage);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage(),e);
        } finally {
            out.close();
            out = null;
        }
    }

    /**
     * @param req
     * @description 构造消息实体
     * @return com.ouyang.fbyx.common.bean.WeChatMsg
     * @author ouyangxingjie
     * @date 2021/7/19 22:01
     */
    private WeChatMsg buildWeChatBaseMsg(HttpServletRequest req){
        WeChatMsg weChatMsg = new WeChatMsg();
        Map<String, String> requestMap = WeChatMsgParseUtil.parseXml(req);
        // 发送方帐号（open_id）
        String fromUserName = requestMap.get("FromUserName");
        weChatMsg.setFromUserName(fromUserName);
        // 公众帐号
        String toUserName = requestMap.get("ToUserName");
        weChatMsg.setToUserName(toUserName);
        // 消息类型
        String msgType = requestMap.get("MsgType");
        weChatMsg.setMsgType(msgType);
        // 消息内容
        String content = requestMap.get("Content");
        weChatMsg.setContent(content);
        // 事件类型
        String event = requestMap.get("Event");
        weChatMsg.setEvent(event);
        log.info("FromUserName is:" + fromUserName + ", ToUserName is:" + toUserName + ", MsgType is:" + msgType);
        return weChatMsg;
    }


    /**
     * @param req
     * @description 校验微信签名
     * @return boolean
     * @author ouyangxingjie
     * @date 2021/7/19 22:01
     */
    private boolean validate(HttpServletRequest req){
        //获取微信加密签名
        String signature = req.getParameter("signature");
        log.info("微信签名是: {}", signature);
        //获取时间戳
        String timestamp = req.getParameter("timestamp");
        //获取随机数
        String nonce = req.getParameter("nonce");
        return SignUtil.checkSignature(signature, timestamp, nonce);
    }

}
