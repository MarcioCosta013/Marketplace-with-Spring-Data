package marcio.marketplace.catalog.infrastructure.event;

import marcio.marketplace.catalog.infrastructure.persistence.entity.EventMetadata;
import marcio.marketplace.common.infrastructure.event.dto.EventUpdated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

@Component
public class EventMetadataEventListener extends AbstractMongoEventListener<EventMetadata> {
    private static final Logger logger = LoggerFactory.getLogger(EventMetadataEventListener.class);

    private final ApplicationEventPublisher publisher;

    public EventMetadataEventListener(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void onAfterSave(AfterSaveEvent<EventMetadata> event){
        logger.info("Event data saved on onAfterSave {}",event.getDocument());
        this.publisher.publishEvent(EventUpdated.from(event.getSource())); //Pegas o que o mongo retorna, transforma em evento e manda pra frente...
    }

    @Override
    public void onAfterDelete(AfterDeleteEvent<EventMetadata> event){
        logger.info("Event data Delete via onAfterDelete {}",event.getDocument());
    }
}
