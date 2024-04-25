package it.unisalento.pasproject.memberservice.repositories;

import it.unisalento.pasproject.memberservice.domain.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResourceRepository extends MongoRepository<Resource, String> {
}
