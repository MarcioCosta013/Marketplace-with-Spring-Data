package marcio.marketplace.catalog.infrastructure.http;

import marcio.marketplace.catalog.application.BrowseShowcaseUseCase;
import marcio.marketplace.catalog.application.dto.EventOutput;
import marcio.marketplace.catalog.domain.Event;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/showcase")
public class ShowcaseController {
    private final BrowseShowcaseUseCase browseShowcaseUseCase;

    public ShowcaseController(BrowseShowcaseUseCase browseShowcaseUseCase) {
        this.browseShowcaseUseCase = browseShowcaseUseCase;
    }

    @GetMapping
    List<EventOutput> browseShowcase(){
        return browseShowcaseUseCase.execute();
    }
}
