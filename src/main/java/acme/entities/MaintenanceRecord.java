
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidMaintenanceRecord;
import acme.datatypes.recordStatus;
import acme.realms.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidMaintenanceRecord
@Table(indexes = {
	@Index(columnList = "id,technician_id, status")
})
public class MaintenanceRecord extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				maintenanceDate;

	@Mandatory
	@Valid
	@Automapped
	private recordStatus		status;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				inspectionDue;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Optional
	@ValidString(min = 0, max = 255)
	@Automapped
	private String				notes;


	@Transient
	public boolean isDraftMode() {
		boolean result;

		result = this.status != recordStatus.COMPLETED;

		return result;
	}


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Technician	technician;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft	aircraft;

}
