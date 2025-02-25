
package acme.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import org.checkerframework.common.aliasing.qual.Unique;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Aircraft extends AbstractEntity {

	public static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	@ValidString(max = 50)
	private String				model;

	@Unique
	@Mandatory
	@Automapped
	@ValidString(max = 50)
	private String				registrationNumber;

	@Mandatory
	@Automapped
	private Integer				capacity;

	@Mandatory
	@Automapped
	@ValidNumber(min = 2000, max = 50000)
	private Integer				cargoWeight;

	@Valid
	@Mandatory
	@Automapped
	private AircraftStatus		status;

	@Optional
	@Automapped
	@ValidString(max = 255)
	private String				details;

	@Mandatory
	@Automapped
	@ManyToOne(optional = false)
	private Airline				airline;

}
