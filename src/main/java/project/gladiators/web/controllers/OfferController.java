package project.gladiators.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.annotations.PageTitle;
import project.gladiators.service.OfferService;

@Controller
@RequestMapping("/top-offers")
public class OfferController extends BaseController {

    private final OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping()
    @PreAuthorize("permitAll()")
    @PageTitle("Top Offers")
    public ModelAndView topOffers(ModelAndView modelAndView) {

        modelAndView.addObject("offers", this.offerService.findAllOffers());

        return super.view("/offer/top-offers", modelAndView);
    }

}
