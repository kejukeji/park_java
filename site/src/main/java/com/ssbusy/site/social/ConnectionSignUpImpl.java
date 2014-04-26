package com.ssbusy.site.social;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.math.RandomUtils;
import org.broadleafcommerce.common.audit.Auditable;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.account.service.MyCustomerService;

public class ConnectionSignUpImpl implements ConnectionSignUp {

	@Resource(name = "blCustomerService")
	protected MyCustomerService customerService;

	@Override
	public String execute(Connection<?> connection) {
		UserProfile userProfile = connection.fetchUserProfile();
		String userName = connection.getKey().getProviderId()+"/"+connection.getKey().getProviderUserId();

		boolean isNewCustomer = false;
		MyCustomer customer = (MyCustomer) customerService
				.readCustomerByUsername(userName);
		if (customer != null) {
			if (customer.isDeactivated())
				return null; // TODO
			Auditable aud = customer.getAuditable();
			aud.setDateUpdated(new Date());
		} else {
			customer = customerService.createNewCustomer();
			isNewCustomer = true;
			Auditable aud = new Auditable();
			aud.setDateCreated(new Date());
			customer.setAuditable(aud);
		}

		customer.setDeactivated(false);
		customer.setRegistered(true);
		customer.setFirstName(userProfile == null
				|| userProfile.getFirstName() == null ? connection
				.getDisplayName() : userProfile.getFirstName());
		customer.setLastName(userProfile == null
				|| userProfile.getLastName() == null ? connection.getKey()
				.getProviderId() : userProfile.getLastName());
		if (userProfile != null)
			customer.setEmailAddress(userProfile.getEmail());
		customer.setUsername(userName);
		customer.setAvatarUrl(connection.getImageUrl());

		if (isNewCustomer) {
			String pwd = "R" + RandomUtils.nextLong()
					+ connection.createData().getAccessToken();
			customerService.registerCustomer(customer, pwd, pwd);
		} else
			customerService.saveCustomer(customer);

		return customer.getUsername();
	}

}
