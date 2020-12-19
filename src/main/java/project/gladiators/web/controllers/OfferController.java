package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.annotations.PageTitle;
import project.gladiators.service.OfferService;
import project.gladiators.web.viewModels.OfferViewModel;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/top-offers")
public class OfferController extends BaseController {

    private final OfferService offerService;
    private final ModelMapper modelMapper;

    @Autowired
    public OfferController(OfferService offerService, ModelMapper modelMapper) {
        this.offerService = offerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    @PreAuthorize("permitAll()")
    @PageTitle("Top Offers")
    public ModelAndView topOffers(ModelAndView modelAndView) {

        modelAndView.addObject("offers", this.offerService.findAllOffers());

        return super.view("/offer/top-offers", modelAndView);
    }

//    @GetMapping("/top-offers/{category}")
//    @ResponseBody
//    public List<OfferViewModel> fetchByCategory(@PathVariable String category) {
//        List<OfferViewModel> offerViewModels = this.offerService.findAllOffers()
//                .stream()
//                .map(o -> this.modelMapper.map(o, OfferViewModel.class))
//                .collect(Collectors.toList());
//
//        return offerViewModels;
//    }
}
