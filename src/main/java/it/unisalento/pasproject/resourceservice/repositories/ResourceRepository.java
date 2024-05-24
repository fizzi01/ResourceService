package it.unisalento.pasproject.resourceservice.repositories;

import it.unisalento.pasproject.resourceservice.domain.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * The ResourceRepository interface is a Spring Data MongoDB repository for the Resource domain object.
 * It extends the MongoRepository interface, which provides methods for CRUD operations.
 * The type of the domain object is Resource, and the type of the ID is String.
 */
public interface ResourceRepository extends MongoRepository<Resource, String> {
}
