package marcio.marketplace.catalog.application.dto;

import marcio.marketplace.catalog.domain.Event;
import marcio.marketplace.catalog.domain.EventMetadata;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record EventOutput(
        String id,
        String title,
        Instant data,
        EventMetadataOutput metadata
) implements Serializable { //O Redis precisa da implementacao do Serializable para fazer a conversao

    public record EventMetadataOutput(
        String eventDescription,
        Map<String, Object> technicalRequirements,
        Map<String, List<SeatOutput>> seatsBySector
    ) implements Serializable {
        public record SeatOutput(
                String id,
                String sectorId,
                BigDecimal price
        ) implements Serializable {
        }

        public static EventMetadataOutput from(EventMetadata metadata){
            Map<String, List<SeatOutput>> seats =
                    metadata.seats().entrySet().stream()
                            .collect(Collectors.toMap(
                                    entry -> entry.getKey().getId().nome(),
                                    entry -> entry.getValue().stream()
                                            .map( seat -> new EventOutput.EventMetadataOutput.SeatOutput(
                                                    seat.getId().number(),
                                                    seat.getSectorId().nome(),
                                                    entry.getKey().getPrice()
                                            ))
                                            .toList()
                            ));

            return new EventOutput.EventMetadataOutput(
                    metadata.eventDestription(),
                    metadata.technicalRequirements(),
                    seats
            );
        }
    }

    public static EventOutput from(Event event){
        return new EventOutput(
                event.getId().id().toString(),
                event.getTitle(),
                event.getData(),
                event.getMetadata().map(EventMetadataOutput::from).orElse(null)
        );
    }
}
