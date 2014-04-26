package org.broadleafcommerce.inventory.service.workflow;

import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.broadleafcommerce.common.logging.SupportLogManager;
import org.broadleafcommerce.common.logging.SupportLogger;
import org.broadleafcommerce.core.workflow.Activity;
import org.broadleafcommerce.core.workflow.ProcessContext;
import org.broadleafcommerce.core.workflow.state.RollbackFailureException;
import org.broadleafcommerce.core.workflow.state.RollbackHandler;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.inventory.exception.ConcurrentInventoryModificationException;
import org.broadleafcommerce.inventory.exception.InventoryUnavailableException;
import org.broadleafcommerce.inventory.service.InventoryService;

/**
 * Provides a standard rollback handler to ensure that in the event of something going wrong in a workflow, that 
 * 
 * @author Kelly Tisdell
 *
 */
public class InventoryRollbackHandler implements RollbackHandler {

    private static final SupportLogger LOG = SupportLogManager.getLogger("broadleaf-oms", InventoryRollbackHandler.class);
    
    public static final String ROLLBACK_BLC_INVENTORY_DECREMENTED = "ROLLBACK_BLC_INVENTORY_DECREMENTED";
    public static final String ROLLBACK_BLC_INVENTORY_INCREMENTED = "ROLLBACK_BLC_INVENTORY_INCREMENTED";
    public static final String ROLLBACK_BLC_ORDER_ID = "ROLLBACK_BLC_ORDER_ID";

    @Resource(name = "blInventoryService")
    protected InventoryService inventoryService;

    protected int maxRetries = 5;

    @Override
    public void rollbackState(Activity<? extends ProcessContext> activity,
            ProcessContext processContext, Map<String, Object> stateConfiguration) throws RollbackFailureException {

        if (stateConfiguration == null ||
                (stateConfiguration.get(ROLLBACK_BLC_INVENTORY_DECREMENTED) == null &&
                stateConfiguration.get(ROLLBACK_BLC_INVENTORY_INCREMENTED) == null)) {
            return;
        }

        String orderId = "(Not Known)";
        if (stateConfiguration.get(ROLLBACK_BLC_ORDER_ID) != null) {
            orderId = String.valueOf(stateConfiguration.get(ROLLBACK_BLC_ORDER_ID));
        }

        @SuppressWarnings("unchecked")
        Map<FulfillmentLocation, InventoryState> inventoryToIncrement = (Map<FulfillmentLocation, InventoryState>) stateConfiguration.get(ROLLBACK_BLC_INVENTORY_DECREMENTED);
        if (inventoryToIncrement != null && !inventoryToIncrement.isEmpty()) {

            Set<FulfillmentLocation> keys = inventoryToIncrement.keySet();

            for (FulfillmentLocation location : keys) {
                int retryCount = 0;

                while (retryCount <= maxRetries) {
                    try {
                        inventoryService.incrementInventory(inventoryToIncrement.get(location).getSkuQuantityMap(), location);
                        inventoryService.incrementInventoryOnHand(inventoryToIncrement.get(location).getSkuQuantityMap(), location);
                        break;
                    } catch (ConcurrentInventoryModificationException ex) {
                        retryCount++;
                        if (retryCount == maxRetries) {
                            LOG.error("After an exception was encountered during checkout, where inventory was decremented. " + maxRetries + " attempts were made to compensate, " +
                                    "but were unsuccessful for order ID: " + orderId + ". This should be corrected manually!", ex);
                        }
                    } catch (RuntimeException ex) {
                        LOG.error("An unexpected error occured in the error handler of the checkout workflow trying to compensate for inventory. This happend for order ID: " +
                                orderId + ". This should be corrected manually!", ex);
                        break;
                    }
                }
            }
        }

        @SuppressWarnings("unchecked")
        Map<FulfillmentLocation, InventoryState> inventoryToDecrement = (Map<FulfillmentLocation, InventoryState>) stateConfiguration.get(ROLLBACK_BLC_INVENTORY_INCREMENTED);
        if (inventoryToDecrement != null && !inventoryToDecrement.isEmpty()) {

            Set<FulfillmentLocation> keys = inventoryToIncrement.keySet();

            for (FulfillmentLocation location : keys) {
                int retryCount = 0;

                while (retryCount <= maxRetries) {
                    try {
                        inventoryService.decrementInventory(inventoryToDecrement.get(location).getSkuQuantityMap(), location);
                        inventoryService.decrementInventoryOnHand(inventoryToDecrement.get(location).getSkuQuantityMap(), location);
                        break;
                    } catch (ConcurrentInventoryModificationException ex) {
                        retryCount++;
                        if (retryCount == maxRetries) {
                            LOG.error("After an exception was encountered during checkout, where inventory was incremented. " + maxRetries + " attempts were made to compensate, " +
                                    "but were unsuccessful for order ID: " + orderId + ". This should be corrected manually!", ex);
                        }
                    } catch (InventoryUnavailableException e) {
                        //This is an awkward, unlikely state.  I just added some inventory, but something happened, and I want to remove it, but it's already gone!
                        LOG.error("While trying roll back (decrement) inventory, we found that there was none left decrement.", e);
                    } catch (RuntimeException ex) {
                        LOG.error("An unexpected error occured in the error handler of the checkout workflow trying to compensate for inventory. This happend for order ID: " +
                                orderId + ". This should be corrected manually!", ex);
                        break;
                    }
                }
            }
        }
    }

    public void setMaxRetries(int maxRetries) {
        if (this.maxRetries < 0) {
            throw new IllegalArgumentException("Max retries cannot be less than 0.");
        }
        this.maxRetries = maxRetries;
    }
}
