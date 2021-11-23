package postbacchatbot.postbacchatbot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import postbacchatbot.postbacchatbot.model.School;

import java.util.List;


@Repository
public interface SchoolRepository extends MongoRepository<School, Integer > {
    List<School> findByName(String name);
}
