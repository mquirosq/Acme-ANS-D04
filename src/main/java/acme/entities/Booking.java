
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidBooking;
import acme.datatypes.TravelClass;
import acme.realms.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidBooking
@Table(indexes = {
	@Index(columnList = "locator_code", unique = true), @Index(columnList = "draft_mode"), @Index(columnList = "customer_id,draft_mode"), @Index(columnList = "purchased_at,customer_id,draft_mode")
})
public class Booking extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,8}$", message = "{acme.validation.booking.locatorCode.pattern}")
	@Column(unique = true)
	private String				locatorCode;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				purchasedAt;

	@Mandatory
	@Valid
	@Automapped
	private TravelClass			travelClass;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				price;

	@Optional
	@ValidString(pattern = "^\\d{4}$", message = "{acme.validation.booking.lastCardNibble.pattern}")
	@Automapped
	private String				lastCardNibble;

	@Mandatory
	// @Valid by default
	@Automapped
	private boolean				draftMode;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Customer			customer;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight				flight;

}
