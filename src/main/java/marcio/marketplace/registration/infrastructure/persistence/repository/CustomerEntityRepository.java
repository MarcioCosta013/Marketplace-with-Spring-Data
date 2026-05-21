package marcio.marketplace.registration.infrastructure.persistence.repository;

import marcio.marketplace.registration.infrastructure.persistence.entity.Customer;
import marcio.marketplace.registration.infrastructure.persistence.entity.projection.CustomerExcerpt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.UUID;

//Essa anotacao (junto com DATA-REST) expoe o repository como controller(CRUDRepository)
@RepositoryRestResource(excerptProjection = CustomerExcerpt.class)
public interface CustomerEntityRepository extends PagingAndSortingRepository<Customer,UUID>, CrudRepository<Customer, UUID> { //PagingAndSortingRepository acrecenta filtros, ordenacao e paginacao para a API...
    /*
    RepositoryRestResource disponibilisa o HATEOAS, é um componente que faz parte da arquitetura REST,
     cujo objetivo é ajudar os clientes a consumirem uma API sem a necessidade de conhecimento prévio.
    */

    List<Customer> findByFirstNameStartingWithIgnoreCase(@Param("firstName") String firstName);

    @Override
    @RestResource(exported = false) //Serve para sobrescrever e nao expor esse endpoint para nao ser usado.
    void deleteById(UUID id);
}
