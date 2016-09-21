
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lee on 9/20/16.
 */
public class User {

    private String name;
    private String password;
    private ArrayList<Message> messages = new ArrayList<>();

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
