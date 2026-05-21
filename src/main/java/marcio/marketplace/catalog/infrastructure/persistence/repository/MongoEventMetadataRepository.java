package marcio.marketplace.catalog.infrastructure.persistence.repository;

import marcio.marketplace.catalog.domain.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class MongoEventMetadataRepository implements EventMetadataRepository {
    private final EventMetadataEntityRepository eventMetadataEntityRepository;

    public MongoEventMetadataRepository(EventMetadataEntityRepository eventMetadataEntityRepository) {
        this.eventMetadataEntityRepository = eventMetadataEntityRepository;
    }

    @Override
    public Optional<EventMetadata> findByEventId(EventId eventId) {
        return eventMetadataEntityRepository.findByEventId(eventId.id()).map(MongoEventMetadataRepository::mapper);
    }

    private static EventMetadata mapper (marcio.marketplace.catalog.infrastructure.persistence.entity.EventMetadata eventMetadata){
        var sectors = eventMetadata.getSectors().stream()
                .map(MongoEventMetadataRepository::mapper)
                .collect(Collectors.toMap(
                        sector -> sector.getId().nome(),
                        Function.identity()
                ));

        var seats = eventMetadata.getSeats().stream()
                .map(MongoEventMetadataRepository::mapper)
                .collect(Collectors.groupingBy(
                        seat -> sectors.get(seat.getSectorId().nome())
                ));

        return new EventMetadata(
                eventMetadata.getEventDescription(),
                eventMetadata.getTechnicalRequirements(),
                seats
        );
    }
    private static Sector mapper(marcio.marketplace.catalog.infrastructure.persistence.entity.EventMetadata.Sector sector){
        return new Sector(new SectorId(sector.getName()), sector.getPrice());
    }

    private static Seat mapper(marcio.marketplace.catalog.infrastructure.persistence.entity.EventMetadata.Seat seat){
        return new Seat(new SeatId(seat.getCode()), new SectorId(seat.getSectorName()));
    }
}
