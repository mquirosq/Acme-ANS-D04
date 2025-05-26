
package acme.realms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidAirlineManager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidAirlineManager
@Table(indexes = {
	@Index(columnList = "identifier_number")
})
public class AirlineManager extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$", message = "{acme.validation.airlineManager.identifierPattern.message}")
	@Column(unique = true)
	private String				identifierNumber;

	@Mandatory
	@ValidNumber(min = 0, max = 120)
	@Automapped
	private Integer				yearsOfExperience;

	@Mandatory
	@ValidMoment(past = true)
	@Automapped
	private Date				birthDate;

	@Optional
	@ValidUrl
	@Automapped
	private String				pictureLink;

}
