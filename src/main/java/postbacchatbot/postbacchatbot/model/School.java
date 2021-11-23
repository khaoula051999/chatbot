package postbacchatbot.postbacchatbot.model;




import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@ToString
@Setter
@Getter
@Document(collection = "School")
public class School  {

    private int id;
    private String name;
    private int seuil ;
    private String date_inscription;
    private String  date_concours;
}
