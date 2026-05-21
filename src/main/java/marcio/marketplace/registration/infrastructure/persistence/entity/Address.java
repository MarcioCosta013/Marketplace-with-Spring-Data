package marcio.marketplace.registration.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@RequiredArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String street, postalCode, city, state;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant creteOn;

    @Override
    public String toString() {
        return String.format("%s, %s %s, %s", street, postalCode, city, state);
    }

}
