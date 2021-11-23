package postbacchatbot.postbacchatbot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import postbacchatbot.postbacchatbot.model.Message;

@Repository

public interface MessageRepository extends MongoRepository<Message, Integer> {


}
