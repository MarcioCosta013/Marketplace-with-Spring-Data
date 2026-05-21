package marcio.marketplace.registration.domain;

import java.util.List;

public interface CustomeRepository {

    public Customer save(Customer customer);

    public List<Customer> findAll();
}
