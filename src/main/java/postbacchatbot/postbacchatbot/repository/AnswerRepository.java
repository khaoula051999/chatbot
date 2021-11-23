package postbacchatbot.postbacchatbot.repository;



import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import postbacchatbot.postbacchatbot.model.Answer;

@Repository
public interface AnswerRepository extends MongoRepository<Answer, Integer> {
}
