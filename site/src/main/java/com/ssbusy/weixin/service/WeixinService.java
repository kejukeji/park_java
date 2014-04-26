package com.ssbusy.weixin.service;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ssbusy.weixin.form.WeixinResTextMessage;
import com.ssbusy.weixin.util.WeixinMessageUtil;


public class WeixinService {

	
	/** 
     * 处理微信发来的请求 
     *  
     * @param request 
     * @return 
     */  
    public static String processRequest(HttpServletRequest request) {  
        String respMessage = null;  
        try {  
            // 默认返回的文本消息内容  
            String respContent = "请求处理异常，请稍候尝试！";  
  
            // xml请求解析  
            Map<String, String> requestMap = WeixinMessageUtil.parseXmlByRequest(request);  
  
            // 发送方帐号（open_id）  
            String fromUserName = requestMap.get("FromUserName");  
            // 公众帐号  
            String toUserName = requestMap.get("ToUserName");  
            // 消息类型  
            String msgType = requestMap.get("MsgType");  
  
            // 回复文本消息  
            WeixinResTextMessage weixinResTextMessage = new WeixinResTextMessage(); 
            weixinResTextMessage.setFromUserName(toUserName);
            weixinResTextMessage.setToUserName(fromUserName);
            weixinResTextMessage.setCreateTime(new Date().getTime());  
            weixinResTextMessage.setMsgType(WeixinMessageUtil.RESP_MESSAGE_TYPE_TEXT);  
            weixinResTextMessage.setFuncFlag(0);  
  
            // 文本消息  
            if (msgType.equals(WeixinMessageUtil.REQ_MESSAGE_TYPE_TEXT)) {  
                respContent = "您发送的是文本消息！";  
            }  
            // 图片消息  
            else if (msgType.equals(WeixinMessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {  
                respContent = "您发送的是图片消息！";  
            }  
            // 地理位置消息  
            else if (msgType.equals(WeixinMessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {  
                respContent = "您发送的是地理位置消息！";  
            }  
            // 链接消息  
            else if (msgType.equals(WeixinMessageUtil.REQ_MESSAGE_TYPE_LINK)) {  
                respContent = "您发送的是链接消息！";  
            }  
            // 音频消息  
            else if (msgType.equals(WeixinMessageUtil.REQ_MESSAGE_TYPE_VOICE)) {  
                respContent = "您发送的是音频消息！";  
            }  
            // 事件推送  
            else if (msgType.equals(WeixinMessageUtil.REQ_MESSAGE_TYPE_EVENT)) {  
                // 事件类型  
                String eventType = requestMap.get("Event");  
                // 订阅  
                if (eventType.equals(WeixinMessageUtil.EVENT_TYPE_SUBSCRIBE)) {  
                    respContent = "谢谢您的关注！";  
                }  
                // 取消订阅  
                else if (eventType.equals(WeixinMessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {  
                    // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息  
                }  
                // 自定义菜单点击事件  
                else if (eventType.equals(WeixinMessageUtil.EVENT_TYPE_CLICK)) {  
                	String eventKey = requestMap.get("EventKey");  
                    if (eventKey.equals("11")) {  
                        respContent = "首页菜单项被点击！";  
                    } else if (eventKey.equals("12")) {  
                        respContent = "分类菜单项被点击！";  
                    } else if (eventKey.equals("13")) {  
                        respContent = "购物车菜单项被点击！";  
                    } else if (eventKey.equals("14")) {  
                        respContent = "我的信息菜单项被点击！";  
                    } else if (eventKey.equals("21")) {  
                        respContent = "优惠活动菜单项被点击！";  
                    } else if (eventKey.equals("22")) {  
                        respContent = "折扣商品菜单项被点击！";  
                    } else if (eventKey.equals("23")) {  
                        respContent = "美妆推荐菜单项被点击！";  
                    } else if (eventKey.equals("24")) {  
                        respContent = "吃喝玩乐菜单项被点击！";  
                    } else if (eventKey.equals("25")) {  
                        respContent = "往期活动菜单项被点击！";  
                    } else if (eventKey.equals("31")) {  
                        respContent = "绑定账户单项被点击！";  
                    } else if (eventKey.equals("32")) {  
                        respContent = "在校客服榜菜单项被点击！";  
                    } else if (eventKey.equals("33")) {  
                        respContent = "操作指令菜单项被点击！";  
                    }    
                }  
            }  
  
            weixinResTextMessage.setContent(respContent);  
            respMessage = WeixinMessageUtil.textMessageToXml(weixinResTextMessage);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return respMessage;  
    }  
}
