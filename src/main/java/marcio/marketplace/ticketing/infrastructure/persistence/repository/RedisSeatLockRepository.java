package marcio.marketplace.ticketing.infrastructure.persistence.repository;

import marcio.marketplace.ticketing.infrastructure.persistence.entity.SeatLock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface RedisSeatLockRepository extends CrudRepository<SeatLock, String> {
    Optional<SeatLock> findByCustomerId(String customerId); //Consulta que faz a verificacao de lock...
}
