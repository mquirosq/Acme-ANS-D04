
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidFlightLeg;
import acme.datatypes.FlightLegStatus;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidFlightLeg
public class FlightLeg extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{3}\\d{4}$", message = "{acme.validation.flightLeg.flightNumberPattern.message}")
	@Column(unique = true)
	private String				flightNumber;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@Valid
	@Automapped
	private FlightLegStatus		status;


	@Transient
	public Double getDuration() {
		double differenceInMiliseconds = this.scheduledArrival.getTime() - this.scheduledDeparture.getTime();
		double differenceInHours = differenceInMiliseconds / (1000 * 60 * 60);
		double result = Math.floor(differenceInHours * 100.0) / 100.0;
		return result;
	}


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport		departureAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport		arrivalAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft	deployedAircraft;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight		parentFlight;

}
