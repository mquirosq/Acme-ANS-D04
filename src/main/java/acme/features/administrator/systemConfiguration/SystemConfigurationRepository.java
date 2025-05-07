
package acme.features.administrator.systemConfiguration;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.SystemConfiguration;

public interface SystemConfigurationRepository extends AbstractRepository {

	@Query("select sc from SystemConfiguration sc")
	SystemConfiguration getSystemConfig();
}
