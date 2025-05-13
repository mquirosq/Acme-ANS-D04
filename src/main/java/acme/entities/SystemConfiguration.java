
package acme.entities;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SystemConfiguration extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "[A-Z]{3}$", message = "{acme.validation.systemConfiguration.systemCurrency.message}")
	@Automapped
	private String				systemCurrency;

	@Mandatory
	@ValidString(pattern = "([A-Z]{3})(, [A-Z]{3})+", min = 1, max = 255, message = "{acme.validation.systemConfiguration.acceptedCurrencies.message}")
	@Automapped
	private String				acceptedCurrencies;
}
