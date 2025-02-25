
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Review extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Size(max = 50)
	private String				name;

	@Mandatory
	@Past
	private Date				postedAt;

	@Mandatory
	@Size(max = 50)
	private String				subject;

	@Mandatory
	@Size(max = 255)
	private String				text;

	@Min(0)
	@Max(10)
	@Optional
	private Double				score;

	@Mandatory
	private Boolean				recommended;
}
