package marcio.marketplace.ticketing.domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
public class Seat {
    private UUID id;
    private SeatId correlationId;

    public Seat(String correlationId) {
        this.id = UUID.randomUUID();
        this.correlationId = new SeatId(correlationId);
    }
}
