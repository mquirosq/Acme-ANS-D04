
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
import acme.realms.AirlineManager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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


	@Transient
	public Date getScheduledDeparture() {
		FlightLegRepository repository = SpringHelper.getBean(FlightLegRepository.class);
		FlightLeg firstLeg = repository.getFirstLegOfFlight(this.getId());
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
		FlightLeg lastLeg = repository.getLastLegOfFlight(this.getId());
		return lastLeg.getScheduledArrival();
	}

	@Transient
	public String getOriginCity() {
		FlightLegRepository repository = SpringHelper.getBean(FlightLegRepository.class);
		FlightLeg firstLeg = repository.getFirstLegOfFlight(this.getId());
		return firstLeg.getDepartureAirport().getCity();
	}

	@Transient
	public String getDestinationCity() {
		FlightLegRepository repository = SpringHelper.getBean(FlightLegRepository.class);
		FlightLeg lastLeg = repository.getLastLegOfFlight(this.getId());
		return lastLeg.getArrivalAirport().getCity();
	}

	@Transient
	public Integer getNumberOfLayovers() {
		FlightLegRepository repository = SpringHelper.getBean(FlightLegRepository.class);
		return repository.getLegsCountOfFlight(this.getId());
	}


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AirlineManager manager;

}
