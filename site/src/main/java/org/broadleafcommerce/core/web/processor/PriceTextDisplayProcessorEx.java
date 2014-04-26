package org.broadleafcommerce.core.web.processor;

import java.text.NumberFormat;

import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.common.web.BroadleafRequestContext;
import org.broadleafcommerce.core.web.processor.PriceTextDisplayProcessor;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.standard.expression.StandardExpressionProcessor;

public class PriceTextDisplayProcessorEx extends PriceTextDisplayProcessor{
   
	protected String getText(Arguments arguments, Element element, String attributeName) {    
        Money price;
        
        try {
            price = (Money) StandardExpressionProcessor.processExpression(arguments, element.getAttributeValue(attributeName));
        } catch (ClassCastException e) {
            Number value = (Number) StandardExpressionProcessor.processExpression(arguments, element.getAttributeValue(attributeName));
            price = new Money(value.doubleValue());
        }

        if (price == null) {
            return "Not Available";
        }
        BroadleafRequestContext brc = BroadleafRequestContext.getBroadleafRequestContext();
        if (brc.getJavaLocale() != null) {
            NumberFormat format = NumberFormat.getCurrencyInstance(brc.getJavaLocale());
           if(price.getCurrency().getCurrencyCode().equals("AUD")){
        	   return "☯ " + price.getAmount().toString();
           }
            format.setCurrency(price.getCurrency());
            return format.format(price.getAmount());
        } else {
          
            return "$ " + price.getAmount().toString();
        }
    }
}
