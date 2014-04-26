package com.ssbusy.controller.alipay;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.core.checkout.service.exception.CheckoutException;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.service.OrderService;
import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipayNotify;
import com.alipay.util.AlipaySubmit;
import com.ssbusy.controller.checkout.CheckoutController;
import com.ssbusy.site.myshippingform.MyBillingInfoForm;

/**
 * 支付宝即时到帐
 * 
 * @author Ju
 */
@Controller
public class AlipayDirectPayController implements InitializingBean {

	private static final String RETURN_INFO_KEY = "return_info";
	private final static Log LOG = LogFactory
			.getLog(AlipayDirectPayController.class);
	/**
	 * 支付类型
	 */
	private final String payment_type = "1";

	/**
	 * 必填，不能修改 服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数
	 */
	private final static String notify_url = "/extern/alipay/notify_url";

	/**
	 * 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/
	 */
	private final static String return_url = "/extern/alipay/return_url";

	/**
	 * 卖家支付宝帐户
	 */
	@Value("${alipay.clientId}")
	private String seller_email = "onxiao@gmail.com";

	@Value("${alipay.partner}")
	private String partner;

	@Value("${alipay.key}")
	private String key;

	@Resource(name = "checkoutController")
	private CheckoutController checkoutController;
	@Resource(name = "blOrderService")
	protected OrderService orderService;
	@Override
	public void afterPropertiesSet() throws Exception {
		AlipayConfig.partner = partner;
		AlipayConfig.key = key;
	}

	/**
	 * 
	 * @param tradeNo
	 *            商户网站订单系统中唯一订单号，必填
	 * @param subject
	 *            订单名称 必填
	 * @param description
	 *            订单描述
	 * @param tradeUrl
	 *            商品展示地址 需以http://开头的完整路径，例如：http://www.xxx.com/myorder.html
	 * @param totalFee
	 *            付款金额 必填
	 * @return 直接返回response body整个内容
	 * @throws IOException
	 */
	@RequestMapping("/alipay/direct_pay")
	public void requestDirectPay(HttpServletResponse resp,
			@RequestParam("tradeNo") String tradeNo,
			@RequestParam("subject") String subject,
			@RequestParam("description") String description,
			@RequestParam("tradeUrl") String tradeUrl,
			@RequestParam("totalFee") BigDecimal totalFee) throws IOException {

		// 把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>(14);
		sParaTemp.put("service", "create_direct_pay_by_user");
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("notify_url", "http://www.onxiao.com" + notify_url);
		sParaTemp.put("return_url", "http://www.onxiao.com" + return_url);
		sParaTemp.put("seller_email", seller_email);
		sParaTemp.put("out_trade_no", tradeNo);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", totalFee.toPlainString());
		sParaTemp.put("body", description);
		sParaTemp.put("show_url", tradeUrl);
		// TODO 防钓鱼时间戳
		String anti_phishing_key = "";
		// 若要使用请调用类文件submit中的query_timestamp函数
		sParaTemp.put("anti_phishing_key", anti_phishing_key);
		// TODO 客户端的IP地址
		String exter_invoke_ip = "";
		// 非局域网的外网IP地址，如：221.0.0.1
		sParaTemp.put("exter_invoke_ip", exter_invoke_ip);

		// 直接返回页面内容
		resp.setContentType("text/html");
		String html = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
		PrintWriter writer = resp.getWriter();
		try {
			writer.print(html);
			writer.flush();
		} finally {
			writer.close();
		}
	}

	/**
	 * 支付宝服务器异步通知页面
	 * 
	 * @param request
	 * @param shopTradeNo
	 *            商户订单号
	 * @param alipayTradeNo
	 * @param tradeStatus
	 *            交易状态
	 * @throws CheckoutException
	 */
	@RequestMapping(notify_url)
	@ResponseBody
	public String notifyPayed(
			Model model,
			HttpServletRequest request,
			HttpServletResponse response, MyBillingInfoForm billingForm,
			BindingResult result,
			@RequestParam(required = false, value = "out_trade_no") String shopTradeNo,
			@RequestParam(required = false, value = "trade_no") String alipayTradeNo,
			@RequestParam(required = false, value = "trade_status") String tradeStatus,
			@RequestParam(required = false, value = "total_fee") String totalFee)
			throws CheckoutException {
		LOG.info("=============================================");
		LOG.info("alipay notifyPayed");
		LOG.info(" shopTradeNo(orderNum) is " + shopTradeNo
				+ ", alipayTradeNo(alipay) is " + alipayTradeNo);
		LOG.info("tradeStatus is " + tradeStatus + ", totalFee is " + totalFee
				+ ";");
		paymentCallback(model, request, response, billingForm, result, shopTradeNo,
				alipayTradeNo, tradeStatus, totalFee);
		
		
		LOG.warn("alipay notifyPayed over,But it is not back to our checkoutController");
		return (String) request.getAttribute(RETURN_INFO_KEY);
	}

	/**
	 * 支付宝页面跳转同步通知页面
	 * 
	 * @param model
	 * @param request
	 * @param shopTradeNo
	 * @param alipayTradeNo
	 * @param tradeStatus
	 * @throws CheckoutException
	 */
	@RequestMapping(return_url)
	public String returnPayed(Model model, HttpServletRequest request,
			HttpServletResponse response, MyBillingInfoForm billingForm, BindingResult result,
			@RequestParam("out_trade_no") String shopTradeNo,
			@RequestParam("trade_no") String alipayTradeNo,
			@RequestParam("trade_status") String tradeStatus,
			@RequestParam("total_fee") String totalFee)
			throws CheckoutException {
		
		LOG.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		LOG.info("alipay returnPayed");
		LOG.info(" shopTradeNo(orderNum) is " + shopTradeNo
				+ ", alipayTradeNo(alipay) is " + alipayTradeNo);
		LOG.info("tradeStatus is " + tradeStatus + ", totalFee is " + totalFee
				+ ";");
		Long orderId;
		try {
			orderId = Long.parseLong(shopTradeNo.substring(12));
		} catch (NumberFormatException e) {
			LOG.error("substring orderNum throws an exception" + shopTradeNo, e);
			return "redirect:/checkout";
		}
		Order cart = orderService.findOrderById(orderId);
		if(cart!=null&& OrderStatus.SUBMITTED.equals(cart.getStatus())){
			return "redirect:/confirmation/"+cart.getOrderNumber();
		}
		return paymentCallback(model, request, response, billingForm, result, shopTradeNo,
				alipayTradeNo, tradeStatus, totalFee);
	}

	@SuppressWarnings("rawtypes")
	private String paymentCallback(Model model, HttpServletRequest request,
			HttpServletResponse response, MyBillingInfoForm billingForm, BindingResult result,
			String shopTradeNo, String alipayTradeNo, String tradeStatus,
			String totalFee) throws CheckoutException {
		String retutn_message = "";
		// 获取支付宝POST过来反馈信息
		Map requestParams = request.getParameterMap();
		Map<String, String> params = new HashMap<String, String>(
				requestParams.size());
		StringBuilder sb = new StringBuilder();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			sb.setLength(0);
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			for (int i = 0; i < values.length; i++) {
				sb.append(values[i]).append(',');
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name,
					values.length > 0 ? sb.substring(0, sb.length() - 1) : "");
		}
		LOG.info("alipay paymentCallback:");
		if (AlipayNotify.verify(params)) {// 验证成功
			if ("TRADE_FINISHED".equals(tradeStatus)
					|| "TRADE_SUCCESS".equals(tradeStatus)) {
				LOG.info("TradeStatus is ：" + tradeStatus + " Trade Fee is "
						+ totalFee);
				retutn_message = "success";
			} else {
				LOG.info("TradeStatus is ：" + tradeStatus + " Trade Fee is "
						+ totalFee);
				retutn_message = "failed";
			}
		} else {// 验证失败
			LOG.info("AlipayNotify.verify(params) is false");
			// model.addAttribute("erorr", "invalid");
			// model.addAttribute("returnInfo", "failed");
			retutn_message = "failed";
		}
		request.setAttribute(RETURN_INFO_KEY, retutn_message);
		LOG.info("use our alipayCheckout");
		return checkoutController.alipayCheckout(request,
				response, model, billingForm, result,
				shopTradeNo, retutn_message, totalFee);

	}
}
