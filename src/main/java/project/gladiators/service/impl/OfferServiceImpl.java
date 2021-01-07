package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import project.gladiators.exceptions.ProductNotFoundException;
import project.gladiators.model.entities.Offer;
import project.gladiators.model.entities.Product;
import project.gladiators.repository.OfferRepository;
import project.gladiators.service.OfferService;
import project.gladiators.service.ProductService;
import project.gladiators.service.serviceModels.OfferServiceModel;
import project.gladiators.service.serviceModels.ProductServiceModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, ProductService productService, ModelMapper modelMapper) {
        this.offerRepository = offerRepository;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<OfferServiceModel> findAllOffers() {
        return this.offerRepository.findAll().stream()
                .map(o -> this.modelMapper.map(o, OfferServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public OfferServiceModel checkIfProductHasOffer(ProductServiceModel productServiceModel) {

        OfferServiceModel offer = new OfferServiceModel();
        this.findAllOffers().forEach(offerServiceModel -> {
            if(offerServiceModel.getProduct().getName().equals(productServiceModel.getName())){
                offer.setPrice(offerServiceModel.getPrice());
                offer.setProduct(productServiceModel);
            }
        });
        return offer;
    }

    @Override
    public BigDecimal getProductOfferPrice(ProductServiceModel productServiceModel) {
        OfferServiceModel offer = this.checkIfProductHasOffer(productServiceModel);
        if(offer.getProduct() != null){
            return offer.getPrice();
        }else {
            return null;
        }
    }

    @Override
    public OfferServiceModel findByProductId(String id) {
        Offer offer = this.offerRepository.findByProduct_Id(id)
                .orElse(null);
        if(offer != null){
            OfferServiceModel offerServiceModel = this.modelMapper
                    .map(offer, OfferServiceModel.class);
            return offerServiceModel;
        }

       return null;
    }

    @Scheduled(fixedRate = 604800000)
    private void generateOffers() {
        this.offerRepository.deleteAll();
        List<ProductServiceModel> products = this.productService.allProducts()
                .stream().filter(productServiceModel -> !productServiceModel.isDeleted())
                .collect(Collectors.toList());

        if (products.isEmpty()) { return; }

        Random random = new Random();
        List<Offer> offers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Offer offer = new Offer();
            offer.setProduct(this.modelMapper.map(products.get(random.nextInt(products.size())), Product.class));
            offer.setPrice(offer.getProduct().getPrice().multiply(new BigDecimal(0.8)));

            if (offers.stream().filter(o -> o.getProduct().getId().equals(offer.getProduct().getId())).count() == 0) {
                offers.add(offer);
            }
        }

        this.offerRepository.saveAll(offers);
    }
}
