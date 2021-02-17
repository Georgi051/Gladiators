package project.gladiators.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.gladiators.service.OfferService;
import project.gladiators.service.serviceModels.OfferServiceModel;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
public class OffersRestController {

    private final OfferService offerService;


    public OffersRestController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<OfferServiceModel>> getAllOffers(){

        return ResponseEntity.ok(offerService.findAllOffers());
    }
}