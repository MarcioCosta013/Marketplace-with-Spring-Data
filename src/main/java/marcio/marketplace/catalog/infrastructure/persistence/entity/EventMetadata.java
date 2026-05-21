package marcio.marketplace.catalog.infrastructure.persistence.entity;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Document
@RequiredArgsConstructor
public class EventMetadata {

    @Id
    private String id;

    @NotNull
    private UUID eventId;

    private String eventDescription;

    private Map<String, Object> technicalRequirements; //isso vai dá muita flexibilidade para o cadastro de eventos podendo colocar aqui coisas diferentes

    private List<Sector> sectors;

    private List<Seat> seats;

    private Instant createdOn;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Sector{
        private String name;
        private BigDecimal price;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Seat{
        private String code, sectorName;
    }
}
