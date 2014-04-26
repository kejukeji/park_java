
UPDATE blc_customer SET user_name=REPLACE(user_name, ':', '/')  
where user_name like 'weibo%' or user_name like 'renren%' or user_name like 'qq%';