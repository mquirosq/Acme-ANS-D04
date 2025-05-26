
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidScore;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidService;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidService
@Table(indexes = {
	@Index(columnList = "promotionCode", unique = true)
})
public class Service extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				name;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				pictureLink;

	@Mandatory
	@ValidNumber(min = 1, max = 100, integer = 3, fraction = 2)
	@Automapped
	private Double				avgDwellTime;

	@Optional
	@ValidString(pattern = "^[A-Z]{4}-[0-9]{2}$", message = "{acme.validation.service.promotionCodePattern.message}")
	@Column(unique = true)
	private String				promotionCode;

	@Optional
	@ValidScore
	@Automapped
	private Double				promotionDiscount;

}
