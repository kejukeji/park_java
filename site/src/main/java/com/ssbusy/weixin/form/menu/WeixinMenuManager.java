package com.ssbusy.weixin.form.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssbusy.weixin.util.WeixinAccessToken;
import com.ssbusy.weixin.util.WeixinUtil;

/**
 * 
 * @author song
 *
 */
public class WeixinMenuManager {
	 private static Logger log = LoggerFactory.getLogger(WeixinMenuManager.class);  
	  
	    public static void main(String[] args) {  
	        // 第三方用户唯一凭证  
	        String appId = "wx6b4742f1d583390a";  
	        // 第三方用户唯一凭证密钥  
	        String appSecret = "8e874b043d9c89fb61ca3e20da9a8a4a";  
	  
	        // 调用接口获取access_token  
	        WeixinAccessToken at = WeixinUtil.getAccessToken(appId, appSecret);  
	  
	        if (null != at) {  
	            // 调用接口创建菜单  
	            int result = WeixinUtil.createMenu(getMenu(), at.getToken());  
	  
	            // 判断菜单创建结果  
	            if (0 == result)  
	                log.info("菜单创建成功！");  
	            else  
	                log.info("菜单创建失败，错误码：" + result);  
	        }  
	    }  
	  
	    /** 
	     * 组装菜单数据 
	     *  
	     * @return 
	     */  
	    private static Menu getMenu() {  
//	        WeixinViewButton btn11 = new WeixinViewButton();  
//	        btn11.setName("首页");  
//	        btn11.setType("view");  
//	        btn11.setUrl("http://www.onxiao.com/weixin/home");  
//	  
//	        WeixinViewButton btn12 = new WeixinViewButton();  
//	        btn12.setName("分类");  
//	        btn12.setType("view");  
//	        btn12.setUrl("http://www.onxiao.com/weixin/category");  
//	  
//	        WeixinViewButton btn13 = new WeixinViewButton();  
//	        btn13.setName("购物车");  
//	        btn13.setType("view");  
//	        btn13.setUrl("http://www.onxiao.com/weixin/cart");  
//	  
//	        WeixinViewButton btn14 = new WeixinViewButton();  
//	        btn14.setName("我的信息");  
//	        btn14.setType("view");  
//	        btn14.setUrl("http://www.onxiao.com/weixin/userinfo");  
	  
	        WeixinCommonButton btn21 = new WeixinCommonButton();  
	        btn21.setName("优惠活动");  
	        btn21.setType("click");  
	        btn21.setKey("21");  
	  
	        WeixinCommonButton btn22 = new WeixinCommonButton();  
	        btn22.setName("折扣商品");  
	        btn22.setType("click");  
	        btn22.setKey("22");  
	  
	        WeixinCommonButton btn23 = new WeixinCommonButton();  
	        btn23.setName("美妆推荐");  
	        btn23.setType("click");  
	        btn23.setKey("23");  
	  
	        WeixinCommonButton btn24 = new WeixinCommonButton();  
	        btn24.setName("吃喝玩乐");  
	        btn24.setType("click");  
	        btn24.setKey("24");  
	  
	        WeixinCommonButton btn25 = new WeixinCommonButton();  
	        btn25.setName("往期活动");  
	        btn25.setType("click");  
	        btn25.setKey("25");  
	  
	        WeixinCommonButton btn31 = new WeixinCommonButton();  
	        btn31.setName("绑定账号");  
	        btn31.setType("click");  
	        btn31.setKey("31");  
	  
	        WeixinCommonButton btn32 = new WeixinCommonButton();  
	        btn32.setName("在校客服");  
	        btn32.setType("click");  
	        btn32.setKey("32");  
	  
	        WeixinCommonButton btn33 = new WeixinCommonButton();  
	        btn33.setName("操作指令");  
	        btn33.setType("click");  
	        btn33.setKey("33");  
	  
	        WeixinViewButton btn12 = new WeixinViewButton();  
	        btn12.setName("在校商城");  
	        btn12.setType("view");  
	        btn12.setUrl("http://www.onxiao.com/weixin/category");  
//	        WeixinComplexButton mainBtn1 = new WeixinComplexButton();  
//	        mainBtn1.setName("在校商城");  
//	        mainBtn1.setSub_button(new WeixinViewButton[] { btn11, btn12, btn13, btn14 });  
	  
	        WeixinComplexButton mainBtn2 = new WeixinComplexButton();  
	        mainBtn2.setName("在校活动");  
	        mainBtn2.setSub_button(new WeixinCommonButton[] { btn21, btn22, btn23, btn24, btn25 });  
	  
	        WeixinComplexButton mainBtn3 = new WeixinComplexButton();  
	        mainBtn3.setName("在校助手");  
	        mainBtn3.setSub_button(new WeixinCommonButton[] { btn31, btn32, btn33 });  
	  


	        Menu menu = new Menu();  
	        menu.setButton(new Button[] { btn12, mainBtn2, mainBtn3 });  
	  
	        return menu;  
	    }  
}
