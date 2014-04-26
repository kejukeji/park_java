package com.ssbusy.controller.sendshortmessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.broadleafcommerce.common.exception.ServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssbusy.core.shortmessage.service.SendShortMessageService;

@Controller
public class SendShortMessageController {
	Map<String, Object> responseMap = new HashMap<String, Object>();
	@RequestMapping(value="/d/app", method = RequestMethod.POST,produces = "application/json")
	public @ResponseBody
	Map<String, Object> sendAppSMS(@RequestParam(value = "pnumber") String pnumber,
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException, ServiceException {
		SendShortMessageService sms=new SendShortMessageService();
		sms.setSmsAccount("LIFkjrewer");
		sms.setSmsPwd("979HIyiy3HrHwf");
		HttpSession session=request.getSession();
		Object count = session.getAttribute("count");
		int i=0;
		String regex = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
		if(pnumber==null||pnumber=="")
		{
			responseMap.put("error", "numberIsNull");
			responseMap.put("errorMessage", "手机号码不能为空，请输入手机号码！");
		}else if(!pnumber.matches(regex)){
				
			responseMap.put("error", "numberUnmatch");
			responseMap.put("errorMessage", "手机号码格式不正确，请重新输入！");			
			
		}else{		
			if(count==null){
				i=1;
				session.setAttribute("count", new Integer(1));
				responseMap.put("errorMessage", "短信已成功发送，请注意查收！"+i);
			}else{
				i = ((Integer) count).intValue();
				i++;
				session.setAttribute("count", new Integer(i));
				if(i>2){
					responseMap.put("error", "overMax");
					responseMap.put("errorMessage", "操作太频繁，请稍后再试！");	
				}else{
					sms.sendSms("APP下载地址为：http://www.onxiao.com/d/app", pnumber);
					responseMap.put("error", "success");
					responseMap.put("errorMessage", "短信已成功发送，请注意查收！"+i);
					System.out.print(i);
				}
			}
		}
		return responseMap;
	}
}
