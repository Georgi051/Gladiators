package project.gladiators.service;


import project.gladiators.service.serviceModels.OfferServiceModel;
import project.gladiators.service.serviceModels.ProductServiceModel;

import java.math.BigDecimal;
import java.util.List;

public interface OfferService {

    List<OfferServiceModel> findAllOffers();

    OfferServiceModel checkIfProductHasOffer(ProductServiceModel productServiceModel);

    BigDecimal getProductOfferPrice(ProductServiceModel productServiceModel);

    OfferServiceModel findByProductId(String id);
}
