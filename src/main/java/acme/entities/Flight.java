
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidFlight;
import acme.realms.AirlineManager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidFlight
public class Flight extends AbstractEntity {

	public static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				requiresSelfTransfer;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;


	@Transient
	public Date getScheduledDeparture() {
		FlightLegRepository repository = SpringHelper.getBean(FlightLegRepository.class);
		FlightLeg firstLeg = repository.getFirstLegOfFlight(this.getId()).orElse(null);
		Date result;
		if (firstLeg == null)
			result = null;
		else
			result = firstLeg.getScheduledDeparture();
		return result;
	}

	@Transient
	public Date getScheduledArrival() {
		FlightLegRepository repository = SpringHelper.getBean(FlightLegRepository.class);
		FlightLeg lastLeg = repository.getLastLegOfFlight(this.getId()).orElse(null);
		Date result;
		if (lastLeg == null)
			result = null;
		else
			result = lastLeg.getScheduledArrival();
		return result;
	}

	@Transient
	public String getOriginCity() {
		FlightLegRepository repository = SpringHelper.getBean(FlightLegRepository.class);
		FlightLeg firstLeg = repository.getFirstLegOfFlight(this.getId()).orElse(null);
		String result;
		if (firstLeg == null)
			result = null;
		else
			result = firstLeg.getDepartureAirport().getCity();
		return result;
	}

	@Transient
	public String getDestinationCity() {
		FlightLegRepository repository = SpringHelper.getBean(FlightLegRepository.class);
		FlightLeg lastLeg = repository.getLastLegOfFlight(this.getId()).orElse(null);
		String result;
		if (lastLeg == null)
			result = null;
		else
			result = lastLeg.getArrivalAirport().getCity();

		return result;
	}

	@Transient
	public String getDestinationCountry() {
		FlightLegRepository repository = SpringHelper.getBean(FlightLegRepository.class);
		FlightLeg lastLeg = repository.getLastLegOfFlight(this.getId()).orElse(null);
		String result;
		if (lastLeg == null)
			result = null;
		else
			result = lastLeg.getArrivalAirport().getCountry();

		return result;
	}

	@Transient
	public Integer getNumberOfLayovers() {
		FlightLegRepository repository = SpringHelper.getBean(FlightLegRepository.class);
		Integer legCount = repository.getLegsCountOfFlight(this.getId());
		if (legCount > 0)
			return legCount - 1;
		else
			return 0;
	}

	@Transient
	public String getIdentifierCode() {
		String identifierCode = " - ";

		if (this.getOriginCity() != null && this.getDestinationCity() != null)
			identifierCode = this.getOriginCity() + " - " + this.getDestinationCity();

		return identifierCode;
	}


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AirlineManager manager;

}
