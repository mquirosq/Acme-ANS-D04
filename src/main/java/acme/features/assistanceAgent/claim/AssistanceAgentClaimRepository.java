
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Claim;
import acme.entities.FlightLeg;
import acme.realms.AssistanceAgent;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where c.agent.id = :assistanceAgentId")
	Collection<Claim> findAllClaimsByAssistanceAgentId(int assistanceAgentId);

	@Query("select c from Claim c where c.id = :id")
	Claim findClaimById(int id);

	@Query("select l from FlightLeg l")
	Collection<FlightLeg> findAllLegs();

	@Query("select a from AssistanceAgent a")
	Collection<AssistanceAgent> findAllAssistanceAgents();
}
