package marcio.marketplace.ticketing.infrastructure.persistence.repository;

import marcio.marketplace.ticketing.infrastructure.persistence.entity.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;
/*
    Como ja tem uma API de Customer poderia sobreescrever, e ficaria duas, entao por isso nao esta sendo exposta como recurso REST,
    e sim apenas como um CrudRepository, para ser usado internamente no sistema de ticketing,
    e nao exposto como um recurso REST, para evitar conflitos com a API de Customer existente.

    E o path "_customers" foi colocado pq quando nao tem um path definido, o Spring Data REST gera um path com o nome
    da entidade, e nesse caso seria "customers", e isso poderia causar conflitos com a API de Customer existente,
    entao foi colocado um path diferente para evitar esse conflito. Ticketing e registration tem Customer como entidades.
 */
@RepositoryRestResource(exported = false, path = "_customers") //
public interface CustomerCrudRepository extends CrudRepository<Customer, UUID> {

}
