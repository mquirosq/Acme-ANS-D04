/*
 * MoneyExchange.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.forms;

import java.util.Date;

import javax.validation.Valid;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidCurrency;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoneyExchange extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Query attributes -------------------------------------------------------

	@Mandatory
	@ValidMoney
	public Money				source;

	@Optional
	@ValidCurrency
	public String				targetCurrency;

	// Response attributes ----------------------------------------------------

	@Optional
	@ValidMoment(past = true)
	public Date					moment;

	@Optional
	@ValidMoney
	public Money				target;

	@Optional
	@Valid
	public Throwable			oops;

	@Optional
	@Valid
	public String				message;

}
