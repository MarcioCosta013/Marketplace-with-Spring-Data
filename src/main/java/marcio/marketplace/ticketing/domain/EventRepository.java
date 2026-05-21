package marcio.marketplace.ticketing.domain;

public interface EventRepository {
    void save (Event event);
    //Como o assento faz parte do evento, ai o sistema de lock abaixo fica aqui também
    boolean existsSeat(EventId eventId, SeatId seatId);
    boolean tryLockSeat(EventId eventId, SeatId seatId, CustomerId customerId);
}
