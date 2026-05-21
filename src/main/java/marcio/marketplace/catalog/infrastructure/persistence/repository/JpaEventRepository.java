package marcio.marketplace.catalog.infrastructure.persistence.repository;

import marcio.marketplace.catalog.domain.Event;
import marcio.marketplace.catalog.domain.EventId;
import marcio.marketplace.catalog.domain.EventRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Repository
public class JpaEventRepository implements EventRepository {
    private final EventEntityRepository eventEntityRepository;

    public JpaEventRepository(EventEntityRepository eventEntityRepository) {
        this.eventEntityRepository = eventEntityRepository;
    }

    @Override
    public List<Event> findAll() {
        var iterable = eventEntityRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(JpaEventRepository::mapper).toList();
    }

    private static Event mapper (marcio.marketplace.catalog.infrastructure.persistence.entity.Event event){
        //Tirando do banco de dados e convertendo para o domain...
        return new Event(new EventId(event.getId()), event.getTitler(),event.getDate(), Optional.empty());
    }
}
