package marcio.marketplace.ticketing.infrastructure.persistence.repository;

import marcio.marketplace.ticketing.domain.*;
import marcio.marketplace.ticketing.infrastructure.persistence.entity.SeatLock;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public class WorkOfUnitRepository implements EventRepository { //"WorkOfUnitRepository" é usado quando se tem mais de um banco de dados aqui...
    private final EventCrudRepository eventCrudRepository;
    private final RedisSeatLockRepository redisSeatLockRepository;

    public WorkOfUnitRepository(EventCrudRepository eventCrudRepository, RedisSeatLockRepository redisSeatLockRepository) {
        this.eventCrudRepository = eventCrudRepository;
        this.redisSeatLockRepository = redisSeatLockRepository;
    }

    @Override
    public void save(Event event) {

        var sectors = event.getSeats().entrySet().stream()
                .map(entry -> {
                    Sector domainSector = entry.getKey();
                    List<Seat> domainSeat = entry.getValue();

                    var seats = domainSeat.stream()
                            .map(s -> new marcio.marketplace.ticketing.infrastructure.persistence.entity.Seat(
                                s.getId(),
                                s.getCorrelationId().id()
                            ))
                            .toList();

                    return new marcio.marketplace.ticketing.infrastructure.persistence.entity.Sector(
                            domainSector.getId(),
                            domainSector.getCorrelationId().id(),
                            domainSector.getPrice(),
                            seats
                    );
                })
                .toList();

        var entity = new marcio.marketplace.ticketing.infrastructure.persistence.entity.Event(
                event.getId(),
                event.getCorrelationId().id(),
                sectors
        );

        eventCrudRepository.save(entity);
    }

    @Override
    public boolean existsSeat(EventId eventId, SeatId seatId) {
        return eventCrudRepository.existsByCorrelationIdAndSectors_Seats_CorrelationId(eventId.id(), seatId.id());
    }

    @Override
    public boolean tryLockSeat(EventId eventId, SeatId seatId, CustomerId customerId) {
        String lockId = eventId.id().toString() + ":" + seatId.id(); //Para trabalhar com Redis tem que criar uma chave...
        if (redisSeatLockRepository.existsById(lockId)) {
            return false;
        }

        var lock = new SeatLock(lockId, customerId.id().toString(), Instant.now());
        redisSeatLockRepository.save(lock);
        return true;
    }
}
