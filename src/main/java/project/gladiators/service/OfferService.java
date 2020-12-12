package project.gladiators.service;


import project.gladiators.service.serviceModels.OfferServiceModel;

import java.util.List;

public interface OfferService {

    List<OfferServiceModel> findAllOffers();

}
