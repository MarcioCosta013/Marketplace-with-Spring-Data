package marcio.marketplace.registration.infrastructure.persistence.repository;

import marcio.marketplace.common.infrastructure.event.dto.CustomerCreated;
import marcio.marketplace.registration.domain.CustomeRepository;
import marcio.marketplace.registration.domain.Customer;
import marcio.marketplace.registration.domain.CustomerId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Repository
public class JpaCustomerRepository implements CustomeRepository {
    private final CustomerEntityRepository customerEntityRepository;

    //Publisher é a forma que o proprio Spring em de se comunicar atráves de eventos...
    private final ApplicationEventPublisher publisher;

    public JpaCustomerRepository(CustomerEntityRepository customerEntityRepository, ApplicationEventPublisher publisher) {
        this.customerEntityRepository = customerEntityRepository;
        this.publisher = publisher;
    }

    @Override
    public Customer save(Customer customer) { //Naó está sendo usado no momento
        var entity = mapper(customer);
        customerEntityRepository.save(entity);

        publisher.publishEvent(new CustomerCreated(customer.getId().toString(), customer.getName()));
        return customer;
    }

    @Override
    public List<Customer> findAll() {
        var interable = customerEntityRepository.findAll();
        return StreamSupport.stream(interable.spliterator(), false)
                .map(JpaCustomerRepository::mapper).toList();

    }

    private static marcio.marketplace.registration.infrastructure.persistence.entity.Customer mapper(Customer customer){
        //transforma o customer do dominio no customer da entidade...
        var entity = new marcio.marketplace.registration.infrastructure.persistence.entity.Customer();

        entity.setId(customer.getId().id());
        entity.setFirstName(customer.getName());
        entity.setEmail(customer.getEmail());

        return entity;
    }

    private static Customer mapper (marcio.marketplace.registration.infrastructure.persistence.entity.Customer entity){

        String fullName = Optional.ofNullable(entity.getLastName())
                .map(lastName -> entity.getFirstName() + " " + lastName)
                .orElseGet(entity::getFirstName);

        return new Customer(new CustomerId(entity.getId()), fullName, entity.getEmail());
    }
}
