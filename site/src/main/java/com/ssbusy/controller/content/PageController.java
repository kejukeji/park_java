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

package com.ssbusy.controller.content;

import org.broadleafcommerce.cms.web.controller.BroadleafPageController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class works in combination with the PageHandlerMapping which finds a category based upon
 * the passed in URL.
 */
@Controller("blPageController")
public class PageController extends BroadleafPageController {
    
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.handleRequest(request, response);
    }

    @RequestMapping("/about/aboutus")
    public String aboutUs(HttpServletRequest request,
			HttpServletResponse response){
		return "content/aboutus";
    }
    @RequestMapping("/about/range-time")
    public String rangeTime(HttpServletRequest request,
			HttpServletResponse response){
		return "content/rangeTime";
    }
    @RequestMapping("/about/payment-type")
    public String paymentType(HttpServletRequest request,
			HttpServletResponse response){
		return "content/paymentType";
    }
    
    @RequestMapping("/about/aftersales")
    public String afterSales(HttpServletRequest request,
			HttpServletResponse response){
		return "content/aftersales";
    }
    
}
