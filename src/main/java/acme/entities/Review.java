
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Review extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	@ValidString(max = 50)
	private String				name;

	@Mandatory
	@Automapped
	@ValidMoment(past = true)
	private Date				postedAt;

	@Mandatory
	@Automapped
	@ValidString(max = 50)
	private String				subject;

	@Mandatory
	@Automapped
	@ValidString(max = 255)
	private String				text;

	@Optional
	@Automapped
	@ValidNumber(min = 0, max = 10)
	private Double				score;

	@Mandatory
	@Automapped
	@Valid
	private Boolean				recommended;
}
