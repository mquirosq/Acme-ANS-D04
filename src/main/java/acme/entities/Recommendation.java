
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidRecommendation;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidRecommendation
@Table(indexes = {
	@Index(columnList = "placeId"), @Index(columnList = "country,city")
})
public class Recommendation extends AbstractEntity {

	public static final long	serialVersionUID	= 1L;

	@Optional
	@ValidString(min = 1, max = 255)
	@Column(unique = true)
	private String				placeId;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				city;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				country;

	@Optional
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				name;

	@Optional
	@ValidUrl
	@Automapped
	private String				website;
}
