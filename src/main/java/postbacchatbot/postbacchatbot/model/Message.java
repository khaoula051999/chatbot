package postbacchatbot.postbacchatbot.model;



import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "Message")
public class Message {


    @Id
    private int id;

    private String text;


    public Message(int id, String text) {

        this.id = id;

        this.text = text;
    }

    public int getId() {
        return id;
    }


    public String getText() {
        return text;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setText(String text) {
        this.text = text;
    }

}
