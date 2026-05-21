package marcio.marketplace.ticketing.infrastructure.persistence.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.Instant;
/*
"timeToLive" significa que quando for criada SeatLock ele vai ficar alocada por 30 segundos
(serve para dá tempo da pessoa fazer uma ordem ou pagamento durante esse tempo)
 */
@RedisHash(value = "seat_lock", timeToLive = 30)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatLock {
    @Id
    private String id;

    @Indexed
    private String customerId;

    private Instant createdAt;

}
