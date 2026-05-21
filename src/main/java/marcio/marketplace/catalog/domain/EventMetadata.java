package marcio.marketplace.catalog.domain;

import java.util.List;
import java.util.Map;

public record EventMetadata(String eventDestription,
                            Map<String, Object> technicalRequirements,
                            Map<Sector, List<Seat>> seats) {

}
