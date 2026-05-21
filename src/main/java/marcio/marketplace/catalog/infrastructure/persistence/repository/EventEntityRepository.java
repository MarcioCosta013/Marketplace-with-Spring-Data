package marcio.marketplace.catalog.infrastructure.persistence.repository;

import marcio.marketplace.catalog.infrastructure.persistence.entity.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource //Essa anotacao (junto com DATA-REST) expoe o repository como controller(CRUDRepository)
public interface EventEntityRepository extends CrudRepository<Event, UUID> {
}
