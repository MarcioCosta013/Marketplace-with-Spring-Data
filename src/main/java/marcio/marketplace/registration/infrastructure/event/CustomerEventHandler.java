package marcio.marketplace.registration.infrastructure.event;

import marcio.marketplace.common.infrastructure.event.dto.CustomerCreated;
import marcio.marketplace.registration.infrastructure.persistence.entity.Customer;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class CustomerEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomerEventHandler.class);

    //Publisher é a forma que o proprio Spring em de se comunicar atráves de eventos...
    private final ApplicationEventPublisher publisher;

    public CustomerEventHandler(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @HandleAfterCreate
    public void handlerAfterCreate(Customer customer){
        logger.warn("CustomerEventHandler#handlerAfterCreate");
        publisher.publishEvent(new CustomerCreated(customer.getId().toString(), customer.getFirstName()));

    }

    @HandleAfterSave
    public void handleAfterSave(Customer customer){
        logger.warn("CustomerEventHandler#handleAfterSave");
    }

    @HandleAfterDelete
    public void handlerAfterDelete(Customer customer){
        logger.warn("CustomerEventHandler#handlerAfterDelete");
    }
}
