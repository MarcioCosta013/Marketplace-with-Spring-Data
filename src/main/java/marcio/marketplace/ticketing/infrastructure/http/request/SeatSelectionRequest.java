package marcio.marketplace.ticketing.infrastructure.http.request;

import marcio.marketplace.ticketing.domain.SeatId;

public record SeatSelectionRequest(String id) {
    public SeatId toInput(){
        return new SeatId(id);
    }
}
