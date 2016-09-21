/**
 * Created by lee on 9/20/16.
 */
public class Message {
    String message;

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
