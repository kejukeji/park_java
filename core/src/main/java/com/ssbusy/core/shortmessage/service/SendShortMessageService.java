package com.ssbusy.core.shortmessage.service;

import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendShortMessageService {
	private AjaxObject ajax = new AjaxObject();
	protected Logger log = LoggerFactory.getLogger(getClass());
	private String smsPwd;
	private String smsAccount;
	class AjaxObject {
		public String request(String url) {
			if (log.isDebugEnabled())
				log.debug("ajax request url=" + url);
			HttpURLConnection connection = null;
			StringWriter writer = null;
			try {
				URL getUrl = new URL(url);
				connection = (HttpURLConnection) getUrl.openConnection();
				connection.connect();
				writer = new StringWriter();
				IOUtils.copy(connection.getInputStream(), writer, "UTF-8");
				return writer.toString();
			} catch (Exception e) {
				log.error("", e);
				return "";
			} finally {
				if (writer != null)
					try {
						writer.close();
					} catch (IOException e) {
						log.error("", e);
					}
				if (connection != null)
					connection.disconnect();
			}
		}
	}
	public boolean sendSms(String msg, String mobile) {
		String str = null;
		try {
			str = ajax.request("http://www.18zjj.com/after/supply/SendApi.aspx?userNick=" + smsAccount + "&userPasswd="
							+ smsPwd + "&smsContent="
							+ URLEncoder.encode(msg, "UTF-8") + "&phoneList=" + mobile);
			if (str != null && str.indexOf("success") >= 0) {
				if (JSONObject.fromObject(str).getInt("number") > 0)
					return true; 
			}
		} catch (Exception e) {
			log.error("fail send sms, response=" + str, e);
		}
		log.info("fail send sms, response=" + str);
		return false;
	}
	public String getSmsPwd() {
		return smsPwd;
	}

	public void setSmsPwd(String smsPwd) {
		this.smsPwd = smsPwd;
	}

	public String getSmsAccount() {
		return smsAccount;
	}

	public void setSmsAccount(String smsAccount) {
		this.smsAccount = smsAccount;
	}

}
