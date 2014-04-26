UPDATE ssb_invoicing si, blc_inventory bi,blc_sku bs 
SET si.RETAIL_PRICE = bs.RETAIL_PRICE, si.SALE_PRICE =bs.SALE_PRICE
WHERE bi.sku_id = bs.sku_id and si.inventory_id = bi.inventory_id ;

alter table ssb_invoicing add column  MAOLI_PRICE  decimal(19,2);

update ssb_invoicing si set si.maoli_price = si.RETAIL_PRICE - si.PURCHASE_PRICE where si.SALE_PRICE is null;

update ssb_invoicing si set si.maoli_price = si.SALE_PRICE - si.PURCHASE_PRICE where si.SALE_PRICE is  not null;

