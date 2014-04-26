package com.ssbusy.payment;

import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.core.checkout.service.workflow.CheckoutContext;
import org.broadleafcommerce.core.checkout.service.workflow.CheckoutSeed;
import org.broadleafcommerce.core.checkout.service.workflow.PaymentServiceActivity;
import org.broadleafcommerce.core.payment.domain.PaymentInfo;
import org.broadleafcommerce.core.payment.domain.PaymentResponseItem;
import org.broadleafcommerce.core.payment.service.CompositePaymentService;
import org.broadleafcommerce.core.payment.service.exception.InsufficientFundsException;
import org.broadleafcommerce.core.payment.service.workflow.CompositePaymentResponse;
import org.springframework.beans.factory.annotation.Value;

public class MyPaymentServiceActivity extends PaymentServiceActivity {
 
	  private static final Log LOG = LogFactory.getLog(PaymentServiceActivity.class);
	@Resource(name="blCompositePaymentService")
	private CompositePaymentService compositePaymentService;
	    
	@Value("${stop.checkout.on.single.payment.failure}")
	protected Boolean stopCheckoutOnSinglePaymentFailure;
	public CheckoutContext execute(CheckoutContext context) throws Exception {
        CheckoutSeed seed = context.getSeedData();
        CompositePaymentResponse response = compositePaymentService.executePayment(seed.getOrder(), seed.getInfos(), seed.getPaymentResponse());
        
        for (Entry<PaymentInfo, PaymentResponseItem> entry : response.getPaymentResponse().getResponseItems().entrySet()) {
            checkTransactionStatus(context, entry.getValue());
            if (context.isStopped()) {
                String log = "Stopping checkout workflow due to payment response code: ";
                log += entry.getValue().getProcessorResponseCode();
                log += " and text: ";
                log += entry.getValue().getProcessorResponseText();
                log += " for payment type: " + entry.getKey().getType().getType();
                LOG.debug(log);
                break;
            }
        }

        // Validate that the total amount collected is not less than the order total
        
        PaymentResponseItem item = response.getPaymentResponse().getResponseItems().values().iterator().next();
       
        Money paidAmount =new Money(item.getCurrency());
       
        for (Entry<PaymentInfo, PaymentResponseItem> entry : response.getPaymentResponse().getResponseItems().entrySet()) {
         
        	if (entry.getValue().getTransactionSuccess()) {
                            
                paidAmount = paidAmount.add(entry.getValue().getTransactionAmount());
            }
        }

        if (paidAmount.lessThan(seed.getOrder().getRemainingTotal())) {
            throw new InsufficientFundsException(String.format("Order remaining total was [%s] but paid amount was [%s]",
                    seed.getOrder().getTotal(), paidAmount));
        }

        return context;
    }
}
