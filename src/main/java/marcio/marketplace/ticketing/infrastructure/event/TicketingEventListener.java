package marcio.marketplace.ticketing.infrastructure.event;

import marcio.marketplace.common.infrastructure.event.dto.CustomerCreated;
import marcio.marketplace.common.infrastructure.event.dto.EventUpdated;
import marcio.marketplace.ticketing.application.CreateCustomerUseCase;
import marcio.marketplace.ticketing.application.CreateEventUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TicketingEventListener {

    private static final Logger logger = LoggerFactory.getLogger(TicketingEventListener.class);

    private final CreateCustomerUseCase createCustomerUseCase;
    private final CreateEventUseCase createEventUseCase;

    public TicketingEventListener(CreateCustomerUseCase createCustomerUseCase, CreateEventUseCase createEventUseCase) {
        this.createCustomerUseCase = createCustomerUseCase;
        this.createEventUseCase = createEventUseCase;
    }

    @EventListener //Para escutar quando o evento do paramentro for "chamado"...
    @Async //para ele usar o virtual thread
    public void handle(CustomerCreated event){ //Aqui passo como parametro o evento que quero ler
        logger.info("CustomerCreated received {}", event);
        createCustomerUseCase.execute(event); //Quando ele receber o evento inicia a regra de negocio do TICKETING...
    }

    @EventListener
    @Async
    public void handle(EventUpdated event){
        logger.info("EventUpdated received {}", event);
        createEventUseCase.execute(event);
    }
}
