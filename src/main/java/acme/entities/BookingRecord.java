
package acme.entities;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.constraints.ValidBookingRecord;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidBookingRecord
@Table(indexes = {
	@Index(columnList = "passenger_id,booking_id", unique = true)
})
public class BookingRecord extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Booking				booking;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Passenger			passenger;
}
