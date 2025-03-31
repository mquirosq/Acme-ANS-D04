
package acme.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.datatypes.ClaimStatus;
import acme.datatypes.ClaimType;
import acme.realms.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Claim extends AbstractEntity {

	public static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				passengerEmail;

	@Mandatory
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private ClaimType			type;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	AssistanceAgent				agent;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	FlightLeg					leg;

	@Mandatory
	@Valid
	@Automapped
	Boolean						isPublished;


	@Transient
	public ClaimStatus getStatus() {
		ClaimStatus out;
		List<TrackingLog> trackingLogs;
		TrackingLogRepository trackingLogRepository;

		out = ClaimStatus.PENDING;
		trackingLogRepository = SpringHelper.getBean(TrackingLogRepository.class);
		trackingLogs = trackingLogRepository.findAllByClaimId(this.getId());

		if (trackingLogs.stream().anyMatch(log -> log.getStatus().equals(ClaimStatus.ACCEPTED)))
			out = ClaimStatus.ACCEPTED;
		else if (trackingLogs.stream().anyMatch(log -> log.getStatus().equals(ClaimStatus.REJECTED)))
			out = ClaimStatus.REJECTED;
		else if (trackingLogs.stream().anyMatch(log -> log.getStatus().equals(ClaimStatus.RECLAIMED)))
			out = ClaimStatus.RECLAIMED;
		return out;
	}
}
