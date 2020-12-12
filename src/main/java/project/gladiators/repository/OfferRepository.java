package project.gladiators.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gladiators.model.entities.Offer;

import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, String> {

    Optional<Offer> findByProduct_Id(String id);
}
