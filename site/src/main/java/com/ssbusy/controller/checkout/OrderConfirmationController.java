/*
 * Copyright 2008-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ssbusy.controller.checkout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.broadleafcommerce.common.email.service.EmailService;
import org.broadleafcommerce.common.email.service.info.EmailInfo;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.service.OrderService;
import org.broadleafcommerce.core.web.controller.checkout.BroadleafOrderConfirmationController;
import org.broadleafcommerce.profile.core.dao.CustomerDao;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class OrderConfirmationController extends BroadleafOrderConfirmationController {

    @Resource(name="blCustomerDao")
    protected CustomerDao customerDao;
    
    @Resource(name = "blEmailService")
    protected EmailService emailService;
    
    @Resource(name = "blOrderConfirmationEmailInfo")
    protected EmailInfo orderConfirmationEmailInfo;

    @Resource(name="blOrderService")
    protected OrderService orderService;

    @RequestMapping(value = "/confirmation/{orderNumber}", method = RequestMethod.GET)
    public String displayOrderConfirmationByOrderNumber(@PathVariable("orderNumber") String orderNumber, Model model,
            HttpServletRequest request, HttpServletResponse response) {
        sendConfirmationEmail(orderNumber);
        DateFormat df = new SimpleDateFormat("HH:mm");
        String firstTime =df.format(new Date().getTime()+20*60*1000);
        String sendTime = firstTime+"~"+df.format(new Date().getTime()+50*60*1000);
        model.addAttribute("sendTime", sendTime);
        return super.displayOrderConfirmationByOrderNumber(orderNumber, model, request, response);
    }
    
    @RequestMapping(value = "/weixin/confirmation/{orderNumber}", method = RequestMethod.GET)
    public String weixinDisplayOrderConfirmationByOrderNumber(@PathVariable("orderNumber") String orderNumber, Model model,
            HttpServletRequest request, HttpServletResponse response) {
    	Customer customer = CustomerState.getCustomer();
        if (customer != null) {
            Order order = orderService.findOrderByOrderNumber(orderNumber);
            if (order != null && customer.equals(order.getCustomer())) {
                model.addAttribute("order", order);
                return "weixin/cart/w_confirmation";
            }
        }
        return null;
    }
    public void sendConfirmationEmail(String orderNumber){
        Order order = orderService.findOrderByOrderNumber(orderNumber);
        Customer customer = customerDao.readCustomerByEmail(order.getEmailAddress());
        if (customer != null){
            HashMap<String, Object> vars = new HashMap<String, Object>();
            vars.put("customer", customer);
            vars.put("orderNumber", orderNumber);
            vars.put("order", order);
            emailService.sendTemplateEmail(customer.getEmailAddress(), getOrderConfirmationEmailInfo(), vars);
        }
    }

    public CustomerDao getCustomerDao() {
        return customerDao;
    }

    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public EmailInfo getOrderConfirmationEmailInfo() {
        return orderConfirmationEmailInfo;
    }

    public void setOrderConfirmationEmailInfo(EmailInfo orderConfirmationEmailInfo) {
        this.orderConfirmationEmailInfo = orderConfirmationEmailInfo;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
