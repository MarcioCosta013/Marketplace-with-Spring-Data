package marcio.marketplace.ticketing.domain;

import org.springframework.util.Assert;

import java.util.UUID;

public record CustomerId(UUID id) {

    public CustomerId{
        Assert.notNull(id, "id most not be null");
    }

    public CustomerId(String id){
        this(UUID.fromString(id));
    }
}
